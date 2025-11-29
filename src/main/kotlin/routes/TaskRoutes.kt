package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter
import isHtmxRequest

fun Route.taskRoutes(pebble: PebbleEngine) {

    /**
     * GET /tasks - Show task list with optional filtering
     */
    get("/tasks") {
        val query = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val data = TaskRepository.search(query = query, page = page, size = 10)

        val template = pebble.getTemplate("tasks/index.peb")

        val model = mapOf(
            "title" to "Tasks",
            "page" to data,
            "query" to query,
            "sessionId" to "dev-session",
            "isHtmx" to call.isHtmxRequest()
        )

        val writer = StringWriter()
        template.evaluate(writer, model)

        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * GET /tasks/fragment - HTMX endpoint for filtered task list
     */
    get("/tasks/fragment") {
        val query = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val data = TaskRepository.search(query = query, page = page, size = 10)

        val listTemplate = pebble.getTemplate("tasks/_list.peb")
        val listWriter = StringWriter()
        listTemplate.evaluate(listWriter, mapOf("page" to data, "query" to query))

        val pagerTemplate = pebble.getTemplate("tasks/_pager.peb")
        val pagerWriter = StringWriter()
        pagerTemplate.evaluate(pagerWriter, mapOf("page" to data, "query" to query))

        val status = """<div id="status" hx-swap-oob="true">Found ${data.totalItems} tasks.</div>"""

        call.respondText(
            listWriter.toString() + pagerWriter.toString() + status,
            ContentType.Text.Html
        )
    }

    /**
     * POST /tasks - Add new task
     */
    post("/tasks") {
        val params = call.receiveParameters()
        val title = params["title"]?.trim()

        if (!title.isNullOrBlank()) {
            TaskRepository.add(title)
        }

        call.respondRedirect("/tasks")
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.NotFound)

        TaskRepository.delete(id)

        if (call.isHtmxRequest()) {
            call.respondText("", ContentType.Text.Html)
        } else {
            call.respondRedirect("/tasks")
        }
    }

    // ==========================
    // Week 7: Inline Editing
    // ==========================

    /**
     * GET /tasks/{id}/edit - Show edit form
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val task = TaskRepository.find(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val errorParam = call.request.queryParameters["error"]

        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmxRequest()) {
            val template = pebble.getTemplate("tasks/partials/edit.peb")

            val model = mapOf(
                "task" to task,
                "error" to errorMessage
            )

            val writer = StringWriter()
            template.evaluate(writer, model)

            call.respondText(writer.toString(), ContentType.Text.Html)

        } else {

            val template = pebble.getTemplate("tasks/index.peb")

            val model = mapOf(
                "title" to "Tasks",
                "tasks" to TaskRepository.all(),
                "editingId" to id,
                "errorMessage" to errorMessage
            )

            val writer = StringWriter()
            template.evaluate(writer, model)

            call.respondText(writer.toString(), ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks/{id}/edit - Save edits
     */
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val task = TaskRepository.find(id)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"]
            .orEmpty()
            .trim()

        // Validate
        if (newTitle.isBlank()) {

            if (call.isHtmxRequest()) {
                val template = pebble.getTemplate("tasks/partials/edit.peb")

                val model = mapOf(
                    "task" to task,
                    "error" to "Title is required. Please enter at least one character."
                )

                val writer = StringWriter()
                template.evaluate(writer, model)

                return@post call.respondText(
                    writer.toString(),
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest
                )
            } else {
                return@post call.respondRedirect("/tasks/${id}/edit?error=blank")
            }
        }

        task.title = newTitle
        TaskRepository.update(task)

        if (call.isHtmxRequest()) {
            val template = pebble.getTemplate("tasks/partials/view.peb")
            val writer = StringWriter()
            template.evaluate(writer, mapOf("task" to task))

            val status = """
                <div id="status" hx-swap-oob="true">
                    Task "${task.title}" updated successfully.
                </div>
            """.trimIndent()

            return@post call.respondText(
                writer.toString() + status,
                ContentType.Text.Html
            )
        }

        call.respondRedirect("/tasks")
    }

    /**
     * GET /tasks/{id}/view - Cancel edit
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val task = TaskRepository.find(id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val template = pebble.getTemplate("tasks/partials/view.peb")

        val model = mapOf("task" to task)

        val writer = StringWriter()
        template.evaluate(writer, model)

        call.respondText(writer.toString(), ContentType.Text.Html)
    }
}

