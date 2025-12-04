package comp2850.instr
package utils
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
/**
 * Simple CSV logger for HCI evaluation metrics.
 * Thread-safe, appends to data/metrics.csv.
 *
 * Privacy: Ensure session_id is anonymous (no PII).
 */
object Logger {
    private val file = File("data/metrics.csv").apply {
        parentFile?.mkdirs()
        if (!exists()) {
            writeText("ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n")
        }
    }
    /**
     * Write a single log entry.
     *
     * @param session Anonymous session ID (e.g., from cookie)
     * @param req Request ID (unique per request, for tracing)
     * @param task Task code (T1_filter, T2_edit, etc.)
     * @param step Event type (start, success, validation_error, fail, server_error)
     * @param outcome Specific outcome for errors (blank_title, max_length, etc.)
     * @param ms Duration in milliseconds (0 if not applicable)
     * @param status HTTP status code (200, 400, 500, etc.)
     * @param js JavaScript mode ("on" or "off")
     */
    @Synchronized
    fun write(
        session: String,
        req: String,
        task: String,
        step: String,
        outcome: String,
        ms: Long,
        status: Int,
        js: String
    ) {
        val ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        file.appendText("$ts,$session,$req,$task,$step,$outcome,$ms,$status,$js\n")
    }
    /**
     * Convenience: log validation error with outcome.
     */
    fun validationError(
        session: String,
        req: String,
        task: String,
        outcome: String,
        ms: Long,
        js: String
    ) {
        write(session, req, task, "validation_error", outcome, ms, 400, js)
    }
    /**
     * Convenience: log success.
     */
    fun success(
        session: String,
        req: String,
        task: String,
        ms: Long,
        js: String
    ) {
        write(session, req, task, "success", "", ms, 200, js)
    }
}
 