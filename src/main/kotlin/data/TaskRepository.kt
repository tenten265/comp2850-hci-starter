package data

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * Simple task data model for Week 6.
 *
 * Week 7 evolution: Add `completed: Boolean`
 * Week 8 evolution: Add `createdAt` timestamp for sorting
 */
data class Task(
    val id: Int,
    var title: String,
)

/**
 * Pagination wrapper for search results
 */
data class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
)

/**
 * In-memory repository with CSV persistence.
 */
object TaskRepository {
    private val file = File("data/tasks.csv")
    private val tasks = mutableListOf<Task>()
    private val idCounter = AtomicInteger(1)

    init {
        file.parentFile?.mkdirs()
        if (!file.exists()) {
            file.writeText("id,title\n")
        } else {
            file.readLines().drop(1).forEach { line ->
                val parts = line.split(",", limit = 2)
                if (parts.size == 2) {
                    val id = parts[0].toIntOrNull() ?: return@forEach
                    tasks.add(Task(id, parts[1]))
                    idCounter.set(maxOf(idCounter.get(), id + 1))
                }
            }
        }
    }

    fun all(): List<Task> = tasks.toList()

    fun add(title: String): Task {
        val task = Task(idCounter.getAndIncrement(), title)
        tasks.add(task)
        persist()
        return task
    }

    fun delete(id: Int): Boolean {
        val removed = tasks.removeIf { it.id == id }
        if (removed) persist()
        return removed
    }

    fun find(id: Int): Task? = tasks.find { it.id == id }

    fun update(task: Task) {
        tasks.find { it.id == task.id }?.let { it.title = task.title }
        persist()
    }

    fun search(query: String, page: Int = 1, size: Int = 10): Page<Task> {
        val filtered = if (query.isBlank()) {
            tasks
        } else {
            tasks.filter { it.title.contains(query, ignoreCase = true) }
        }

        val totalItems = filtered.size
        val totalPages = (totalItems + size - 1) / size
        val clampedPage = page.coerceIn(1, maxOf(1, totalPages))
        val startIdx = (clampedPage - 1) * size
        val endIdx = minOf(startIdx + size, totalItems)
        val items = filtered.subList(startIdx, endIdx)

        return Page(
            items = items,
            currentPage = clampedPage,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }

    private fun persist() {
        file.writeText("id,title\n" + tasks.joinToString("\n") { "${it.id},${it.title}" })
    }
}