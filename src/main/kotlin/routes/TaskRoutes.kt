package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter

fun Route.taskRoutes() {

    val pebble =
        PebbleEngine
            .Builder()
            .loader(
                io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                    prefix = "templates/"
                },
            )
            .build()

    /**
     * Helper: Check if request is from HTMX
     */
    fun ApplicationCall.isHtmx(): Boolean =
        request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    /**
     * GET /tasks - List all tasks
     */
    get("/tasks") {
        val model = mapOf(
            "title" to "Tasks",
            "tasks" to TaskRepository.all()
        )

        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)

        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * POST /tasks - Add new task
     */
    post("/tasks") {
        val title = call.receiveParameters()["title"]
            .orEmpty()
            .trim()

        if (title.isBlank()) {
            if (call.isHtmx()) {
                val error = """
                    <div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                        Title is required. Please enter at least one character.
                    </div>
                """.trimIndent()

                return@post call.respondText(
                    error,
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest
                )
            } else {
                call.response.headers.append("Location", "/tasks")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        val task = TaskRepository.add(title)

        if (call.isHtmx()) {
            val template = pebble.getTemplate("tasks/_item.peb")
            val writer = StringWriter()
            template.evaluate(writer, mapOf("task" to task))

            val status = """
                <div id="status" hx-swap-oob="true">
                    Task "${task.title}" added successfully.
                </div>
            """.trimIndent()

            return@post call.respondText(
                writer.toString() + status,
                ContentType.Text.Html,
                HttpStatusCode.Created
            )
        }

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
        val removed = id?.let { TaskRepository.delete(it) } ?: false

        if (call.isHtmx()) {
            val message = if (removed) "Task deleted." else "Could not delete task."

            val status = """
                <div id="status" hx-swap-oob="true">
                    $message
                </div>
            """.trimIndent()

            return@post call.respondText(status, ContentType.Text.Html)
        }

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
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

        if (call.isHtmx()) {
            val template = pebble.getTemplate("tasks/_edit.peb")
            val writer = StringWriter()

            template.evaluate(writer, mapOf(
                "task" to task,
                "error" to errorMessage
            ))

            call.respondText(writer.toString(), ContentType.Text.Html)
        } else {
            val template = pebble.getTemplate("tasks/index.peb")
            val writer = StringWriter()

            template.evaluate(writer, mapOf(
                "title" to "Tasks",
                "tasks" to TaskRepository.all(),
                "editingId" to id,
                "errorMessage" to errorMessage
            ))

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

        // Validation
        if (newTitle.isBlank()) {

            if (call.isHtmx()) {
                val template = pebble.getTemplate("tasks/_edit.peb")
                val writer = StringWriter()

                template.evaluate(writer, mapOf(
                    "task" to task,
                    "error" to "Title is required. Please enter at least one character."
                ))

                return@post call.respondText(
                    writer.toString(),
                    ContentType.Text.Html,
                    HttpStatusCode.BadRequest
                )
            } else {
                return@post call.respondRedirect("/tasks/$id/edit?error=blank")
            }
        }

        task.title = newTitle
        TaskRepository.update(task)

        if (call.isHtmx()) {
            val template = pebble.getTemplate("tasks/_item.peb")
            val writer = StringWriter()

            template.evaluate(writer, mapOf("task" to task))

            val status = """
                <div id="status" hx-swap-oob="true">
                    Task "${task.title}" updated successfully.
                </div>
            """.trimIndent()

            call.respondText(
                writer.toString() + status,
                ContentType.Text.Html
            )

            return@post
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

        val template = pebble.getTemplate("tasks/_item.peb")
        val writer = StringWriter()

        template.evaluate(writer, mapOf("task" to task))
        call.respondText(writer.toString(), ContentType.Text.Html)
    }
}
