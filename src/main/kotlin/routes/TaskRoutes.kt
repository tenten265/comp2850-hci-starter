import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.routing.*
import pebble.PebbleRender
import repository.TaskRepository
import isHtmxRequest

fun Route.taskRoutes(repo: TaskRepository) {

    // ===========================
    // GET /tasks (full page)
    // ===========================
    get("/tasks") {
        val error = call.request.queryParameters["error"]
        val msg = call.request.queryParameters["msg"]
        val q = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1

        val data = repo.search(q, page)

        val model = mapOf(
            "title" to "Tasks",
            "page" to data,
            "query" to q,
            "error" to error,
            "msg" to msg
        )

        val html = PebbleRender.render("tasks/index.peb", model)
        call.respondText(html, ContentType.Text.Html)
    }

    // ===========================
    // HTMX fragment: /tasks/fragment
    // ===========================
    get("/tasks/fragment") {
        val q = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val data = repo.search(q, page)

        val list = PebbleRender.render("tasks/_list.peb", mapOf("page" to data))
        val pager = PebbleRender.render("tasks/_pager.peb", mapOf("page" to data, "query" to q))

        call.respondText(list + pager, ContentType.Text.Html)
    }

    // ===========================
    // POST /tasks (create)
    // ===========================
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

        // Validation: blank
        if (title.isBlank()) {
            if (call.isHtmxRequest()) {
                val status = """<div id="status" role="alert" hx-swap-oob="true">Title is required.</div>"""
                return@post call.respondText(status, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                return@post call.respondRedirect("/tasks?error=title")
            }
        }

        // Validation: too long
        if (title.length > 200) {
            if (call.isHtmxRequest()) {
                val status = """<div id="status" role="alert" hx-swap-oob="true">Title too long (max 200 chars).</div>"""
                return@post call.respondText(status, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                return@post call.respondRedirect("/tasks?error=title&msg=too_long")
            }
        }

        // Success path
        val task = repo.add(title)

        if (call.isHtmxRequest()) {
            val fragment = PebbleRender.render("tasks/_item.peb", mapOf("task" to task))
            val status = """<div id="status" role="alert" hx-swap-oob="true">Added "${task.title}".</div>"""
            return@post call.respondText(fragment + status, ContentType.Text.Html)
        }

        call.respondRedirect("/tasks")
    }

    // ===========================
    // GET /tasks/{id}/edit
    // ===========================
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest)

        val task = repo.find(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val partial = PebbleRender.render("tasks/partials/edit.peb", mapOf("task" to task))
        call.respondText(partial, ContentType.Text.Html)
    }

    // ===========================
    // POST /tasks/{id}/edit
    // ===========================
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest)

        val task = repo.find(id)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation blank
        if (newTitle.isBlank()) {
            if (call.isHtmxRequest()) {
                val html = PebbleRender.render("tasks/partials/edit.peb",
                    mapOf("task" to task, "error" to "Title is required"))
                return@post call.respondText(html, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                return@post call.respondRedirect("/tasks?error=title")
            }
        }

        // Validation too long
        if (newTitle.length > 200) {
            if (call.isHtmxRequest()) {
                val html = PebbleRender.render("tasks/partials/edit.peb",
                    mapOf("task" to task, "error" to "Title too long (max 200 chars)"))
                return@post call.respondText(html, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                return@post call.respondRedirect("/tasks?error=title&msg=too_long")
            }
        }

        val updated = task.copy(title = newTitle)
        repo.update(updated)

        if (call.isHtmxRequest()) {
            val html = PebbleRender.render("tasks/_item.peb", mapOf("task" to updated))
            val status = """<div id="status" role="alert" hx-swap-oob="true">Updated "${updated.title}".</div>"""
            return@post call.respondText(html + status, ContentType.Text.Html)
        }

        call.respondRedirect("/tasks")
    }

    // ===========================
    // DELETE (HTMX)
    // ===========================
    delete("/tasks/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest)

        val task = repo.find(id)
        repo.delete(id)

        val status = """<div id="status" role="alert" hx-swap-oob="true">Deleted "${task?.title ?: "task"}".</div>"""
        call.respondText(status, ContentType.Text.Html)
    }

    // ===========================
    // POST fallback (no-JS delete)
    // ===========================
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest)

        repo.delete(id)
        call.respondRedirect("/tasks")
    }
}
