package routes
 
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodeURLParameter
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import model.Task
import model.ValidationResult
import renderTemplate
import storage.TaskStore
import utils.*
 
private const val PAGE_SIZE = 10
 
fun Routing.configureTaskRoutes(store: TaskStore = TaskStore()) {
 
    // --- List tasks ---
    get("/tasks") { call.handleTaskList(store) }
    get("/") { call.respondRedirect("/tasks") }
 
    // --- HTMX fragment for filtering / pagination ---
    get("/tasks/fragment") { call.handleTaskFragment(store) }
 
    // --- Create task ---
    post("/tasks") { call.handleCreateTask(store) }
 
    // --- Edit task ---
    get("/tasks/{id}/edit") { call.handleEditTask(store) }
    post("/tasks/{id}/edit") { call.handleUpdateTask(store) }
 
    // --- View / Cancel edit ---
    get("/tasks/{id}/view") { call.handleViewTask(store) }
 
    // --- Toggle complete ---
    post("/tasks/{id}/toggle") { call.handleToggleTask(store) }
 
    // --- Delete task ---
    delete("/tasks/{id}") { call.handleDeleteTask(store) }      // HTMX
    post("/tasks/{id}/delete") { call.handleDeleteTask(store) } // No-JS fallback
 
    // --- Search / filter tasks ---
    get("/tasks/search") { call.handleSearchTasks(store) }
}
 
// ===================
// === Handlers ======
// ===================
 
private suspend fun ApplicationCall.handleTaskList(store: TaskStore) {
    timed("T0_list", jsMode()) {
        val page = requestedPage()
        val query = requestedQuery()
        val paginated = paginateTasks(store, query, page)
        val html = renderTemplate("tasks/index.peb", paginated.context)
        respondText(html, ContentType.Text.Html)
    }
}
 
private suspend fun ApplicationCall.handleTaskFragment(store: TaskStore) {
    timed("T1_filter", jsMode()) {
        val page = requestedPage()
        val query = requestedQuery()
        val paginated = paginateTasks(store, query, page)
        val statusHtml = filterStatusFragment(query, paginated.page.totalItems)
 
        if (!isHtmxRequest()) {
            respondRedirect(redirectPath(query, page))
        } else {
            respondTaskArea(paginated, statusHtml)
        }
    }
}
 
private suspend fun ApplicationCall.handleCreateTask(store: TaskStore) {
    val reqId = newReqId()
    val session = request.cookies["sid"] ?: "anon"
    val jsMode = jsMode()
 
    timed("T3_add", jsMode) {
        val params = receiveParameters()
        val title = params["title"].orEmpty().trim()
        val query = params["q"].orEmpty().trim()
 
        // --- Validation ---
        when (val validation = Task.validate(title)) {
            is ValidationResult.Error -> {
                val outcome = when {
                    title.isBlank() -> "blank_title"
                    title.length > Task.MAX_TITLE_LENGTH -> "max_length"
                    title.length < Task.MIN_TITLE_LENGTH -> "min_length"
                    else -> "invalid_title"
                }
                Logger.validationError(session, reqId, "T3_add", outcome, 0, jsMode)
 
                if (isHtmxRequest()) {
                    val paginated = paginateTasks(store, query, 1)
                    val statusHtml = messageStatusFragment(validation.message, isError = true)
                    respondTaskArea(paginated, statusHtml)
                } else {
                    respondRedirect("/tasks?error=title")
                }
            }
            ValidationResult.Success -> {
                val startTime = System.currentTimeMillis()
                val task = store.add(Task(title = title))
                val duration = System.currentTimeMillis() - startTime
                Logger.success(session, reqId, "T3_add", duration, jsMode)
 
                if (isHtmxRequest()) {
                    val paginated = paginateTasks(store, query, 1)
                    val statusHtml = messageStatusFragment("""Task "${task.title}" added successfully.""")
                    respondTaskArea(paginated, statusHtml, htmxTrigger = "task-added")
                } else {
                    respondRedirect("/tasks")
                }
            }
        }
    }
}
 
private suspend fun ApplicationCall.handleEditTask(store: TaskStore) {
    val id = parameters["id"] ?: run {
        respond(HttpStatusCode.BadRequest)
        return
    }
 
    val task = store.getById(id)
    if (task == null) {
        respond(HttpStatusCode.NotFound)
        return
    }
 
    if (isHtmxRequest()) {
        val html = renderTemplate("tasks/_edit.peb", mapOf("task" to task.toPebbleContext()))
        respondText(html, ContentType.Text.Html)
    } else {
        respondRedirect("/tasks")
    }
}
 
private suspend fun ApplicationCall.handleUpdateTask(store: TaskStore) {
    val id = parameters["id"] ?: run {
        respond(HttpStatusCode.BadRequest)
        return
    }
 
    val task = store.getById(id) ?: run {
        respond(HttpStatusCode.NotFound)
        return
    }
 
    val newTitle = receiveParameters()["title"]?.trim().orEmpty()
    val validation = Task.validate(newTitle)
 
    if (validation is ValidationResult.Error) {
        if (isHtmxRequest()) {
            val html = renderTemplate("tasks/_edit.peb", mapOf("task" to task.toPebbleContext(), "error" to validation.message))
            respondText(html, ContentType.Text.Html)
        } else {
            respondRedirect("/tasks")
        }
        return
    }
 
    val updated = task.copy(title = newTitle)
    store.update(updated)
 
    if (isHtmxRequest()) {
        val html = renderTemplate("tasks/_item.peb", mapOf("task" to updated.toPebbleContext()))
        val status = """<div id="status" hx-swap-oob="true" role="status">Task updated successfully.</div>"""
        respondText(html + status, ContentType.Text.Html)
    } else {
        respondRedirect("/tasks")
    }
}
 
private suspend fun ApplicationCall.handleViewTask(store: TaskStore) {
    val id = parameters["id"] ?: run {
        respond(HttpStatusCode.BadRequest)
        return
    }
 
    val task = store.getById(id) ?: run {
        respond(HttpStatusCode.NotFound)
        return
    }
 
    if (isHtmxRequest()) {
        val html = renderTemplate("tasks/_item.peb", mapOf("task" to task.toPebbleContext()))
        respondText(html, ContentType.Text.Html)
    } else {
        respondRedirect("/tasks")
    }
}
 
private suspend fun ApplicationCall.handleToggleTask(store: TaskStore) {
    timed("T2_edit", jsMode()) {
        val id = parameters["id"] ?: run {
            respond(HttpStatusCode.BadRequest)
            return@timed
        }
 
        val updated = store.toggleComplete(id)
        if (updated == null) {
            respond(HttpStatusCode.NotFound, "Task not found")
            return@timed
        }
 
        if (isHtmxRequest()) {
            val html = renderTemplate("tasks/_item.peb", mapOf("task" to updated.toPebbleContext()))
            val statusText = if (updated.completed) "marked complete" else "marked incomplete"
            val statusHtml = messageStatusFragment("""Task "${updated.title}" $statusText.""")
            respondText(html + "\n" + statusHtml, ContentType.Text.Html)
        } else {
            respondRedirect("/tasks")
        }
    }
}
 
private suspend fun ApplicationCall.handleDeleteTask(store: TaskStore) {
    timed("T4_delete", jsMode()) {
        val id = parameters["id"] ?: run {
            respond(HttpStatusCode.BadRequest)
            return@timed
        }
 
        val task = store.getById(id)
        val deleted = store.delete(id)
 
        if (!deleted)