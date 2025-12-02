# Evaluation Metrics — Week 9

## Quantitative Measures

### 1. Time-on-Task (Efficiency)
**Definition**: Duration from task start to successful completion.
**How to collect**: Server-side instrumentation (DurationMs in `metrics.csv`).
**Analysis**: **Median** (primary, resistant to outliers) and **Median Absolute Deviation (MAD)**.

---

### 2. Task Success Rate (Effectiveness)
**Definition**: Percentage of participants completing the task correctly without help.
**How to collect**: Binary (1 = success, 0 = fail/abandon) from manual observation + server logs (`step=success`).
**Analysis**: Success percentage per task, compared across JS-on vs JS-off modes.

---

### 3. Error Rate (Accuracy)
**Definition**: Proportion of attempts that trigger validation errors.
**How to collect**: Server logs (`step=validation_error` / `task_attempt_count`).
**Analysis**: Percentage per task, categorized by error type (e.g., `blank_title`, `max_length`).

---

### 4. Mode Comparison (HTMX vs No-JS)
**Definition**: Comparison of Time-on-Task and Success Rate between JS-on and JS-off groups.
**How to collect**: Instrumentation logs (`js_mode` column).
**Analysis**: Compare median times and success rates, noting expected performance drop for No-JS.

---

## Qualitative Measures

### 5. Post-Task Confidence (Subjective)
**Definition**: Self-reported confidence that task was completed correctly (1=low, 5=high).
**How to collect**: Verbal question immediately after each task (manual recording).
**Analysis**: **Mean** score per task (used for Likert scales). Low confidence despite success indicates poor feedback.

---

### 6. Think-Aloud Observations & Facilitator Notes
**Definition**: Verbal comments and observed behaviours (hesitations, confusion, workarounds).
**How to collect**: Manual note-taking during pilot (verbatim quotes, timestamps).
**Analysis**: Thematic coding to identify patterns (e.g., "Success feedback insufficient").

---

### 7. Accessibility Barriers (Observational)
**Definition**: Specific moments where keyboard or screen reader use is blocked or impaired.
**How to collect**: Manual observation during dedicated keyboard/SR sessions.
**Analysis**: List barriers and map directly to WCAG criteria (e.g., 4.1.3 Status Messages).