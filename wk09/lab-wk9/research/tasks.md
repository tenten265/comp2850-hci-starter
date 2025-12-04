# Evaluation Tasks — Week 9

## Task T1: Filter Tasks

**Scenario**:
"You need to quickly locate all tasks related to reports so you can review your workload. Use the filter box to show tasks containing the word ‘report’, then tell us how many remain."

**Setup**:
- Pre-populate task list with 10 tasks, 3 containing "report" in title.

**Success criteria**:
- Participant types "report" into filter box
- Only the 3 matching tasks remain
- Participant reports correct count
- Completed within 2 minutes
- No validation errors

**Metrics**: Time-on-task, Completion (1/0), Validation errors (count), Confidence rating (1–5)
**Inclusion focus**: Result count announced by screen reader, Keyboard-only flow fully operable, Focus visible when navigating filter input, Works with JS disabled, Screen reader only: verify skip link announcement when navigating to main content.

---

## Task T2: Edit Task Title

**Scenario**:
"The task ‘Submit invoices’ needs updating. Change it to ‘Submit invoices by Friday’ and save the change."

**Setup**:
- Task ID 5: "Submit invoices" exists in list
- Participant must click Edit, change text, save

**Success criteria**:
- Participant activates edit mode
- Title updated correctly
- Change persists after saving
- Completed within 90 seconds
- No validation errors (unless intentionally triggered)

**Metrics**: Time from clicking Edit to save confirmation (ms), Completion (0/1), Validation errors (count), Confidence rating (1–5)
**Inclusion focus**: Status message “Updated [title]” announced (SR), Focus returns to or near edited task, Keyboard-only flow works, Works with JS disabled, If error triggered: error announcement is assertive.

---

## Task T3: Add New Task (Updated for Error Testing)

**Scenario**:
"You need to add the reminder ‘Call supplier about delivery’. First, attempt to submit the form **blank** to test the error handling, then submit the correct title."

**Setup**:
- Add-task form visible at top of page

**Success criteria**:
- **First attempt: participant submits the form blank (intentional error)**
- **System displays error message**
- **Second attempt: participant types task title and submits**
- New task appears in list
- Completed within 60 seconds (from initial focus to successful confirmation)

**Metrics**: Time from initial focus to successful confirmation (ms), Completion (0/1), **Validation errors (count—must be ≥ 1)**, Confidence rating (1–5)
**Inclusion focus**: Error message announced assertively (SR), **Error message persists in no-JS mode (PRG test)**, Form remains usable after error, Success status message announced, Works with keyboard only, Works with JS disabled.

---

## Task T4: Delete Task (Updated for Accessibility Contrast)

**Scenario**:
"The task ‘Test entry’ is no longer needed. Delete it from the list."

**Setup**:
- Task ID 8: “Test entry” present in list

**Success criteria**:
- Participant clicks Delete
- Confirms deletion (HTMX path) OR submits fallback (no-JS)
- Task is removed
- Completed within 45 seconds

**Metrics**: Time from click Delete to final confirmation (ms), Completion (0/1), Confidence rating (1–5)
**Inclusion focus**: Delete button has accessible name (“Delete task: Test entry”), Screen reader announces “Deleted [title]”, Keyboard-only works, **Check delete button contrast >4.5:1**, Works with JS disabled (no dialog, but deletion still functions).


## Task Order

**Recommended sequence**:
1. **Warm-up** (not timed): "Browse the task list and familiarize yourself with the interface."
2. T3 (Add) — Low cognitive load, builds confidence
3. T1 (Filter) — Medium complexity, tests search
4. T2 (Edit) — Tests inline interaction, validation
5. T4 (Delete) — Destructive action, tests confirmation
6. **Debrief** (qualitative): Open-ended questions

**Counterbalance** if testing multiple participants: alternate T1/T2 order to avoid learning effects.

---

## Notes for Facilitator

- **Do not help** unless participant is completely stuck (>3 min). Note as "facilitated" in observations.
- **Think-aloud optional**: Ask participants to narrate their thoughts if comfortable. Don't force.
- **Screen reader users**: Allow extra time for navigation. Log SR-specific observations separately.
- **Keyboard-only**: Offer keyboard-only variant to 1-2 participants for comparison.
- **No-JS**: Test at least 1 participant with JS disabled to verify parity.

---

## Success Definitions

**Completion codes**:
- `1` = Task fully completed, correct outcome
- `0.5` = Partial completion (e.g., found filter but wrong count)
- `0` = Failed or abandoned

**Time bounds**:
- T1: 120s
- T2: 90s
- T3: 60s
- T4: 45s

If participant exceeds time, prompt: "Would you like to continue, or shall we move to the next task?"