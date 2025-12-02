# Evaluation Tasks — Week 9

## Task T1: Filter Tasks

**Scenario**:
"You've been asked to find all tasks containing the word 'report'. Use the filter box to show only matching tasks, then count how many tasks remain."

**Setup**:
- Pre-populate task list with 10 tasks, 3 containing "report" in title.

**Success criteria**:
- Participant uses filter box (types "report")
- Participant reports correct count (3 tasks)
- Completed within 2 minutes
- No validation errors

**Metrics**: Time-on-task, Completion (1/0), Validation errors (count), Confidence rating (1–5)
**Inclusion focus**: Screen reader announcement of result count, Keyboard-only completion possible, No-JS parity.

---

## Task T2: Edit Task Title

**Scenario**:
"The task 'Submit invoices' has a typo. Change it to 'Submit invoices by Friday' and save the change."

**Setup**:
- Task ID 5: "Submit invoices" (visible in list)
- Participant must click Edit, change text, save

**Success criteria**:
- Participant activates edit mode
- Participant updates title correctly
- Change persists after save
- Completed within 90 seconds
- No validation errors

**Metrics**: Time-on-task, Completion (1/0), Validation errors (count), Confidence rating (1–5)
**Inclusion focus**: Focus management during edit/save, accessible status message (updated), keyboard-only editing.

---

## Task T3: Add New Task

**Scenario**:
"You need to remember to 'Call supplier about delivery'. Add this as a new task."

**Setup**:
- Form visible at top of page

**Success criteria**:
- Participant types exact title (or close match)
- Submits form
- New task appears in list
- Completed within 60 seconds

**Metrics**: Time-on-task, Completion (1/0), Validation errors (count), Confidence rating (1–5)
**Inclusion focus**: Accessible status message (added), dual-path functionality (HTMX vs No-JS PRG), error handling visibility.

---

## Task T4: Delete Task

**Scenario**:
"The task 'Test entry' is no longer needed. Delete it from the list."

**Setup**:
- Task ID 8: "Test entry" (visible in list)

**Success criteria**:
- Participant clicks Delete button
- Confirms deletion (HTMX path) or submits form (no-JS)
- Task removed from list
- Completed within 45 seconds

**Metrics**: Time-on-task, Completion (1/0), Confidence rating (1–5)
**Inclusion focus**: Accessible name for Delete button, confirmation dialog (HTMX), No-JS parity trade-off (no confirmation).