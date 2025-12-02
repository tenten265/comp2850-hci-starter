# Peer Pilot Protocol — Week 9

**Module**: COMP2850 HCI
**Activity**: Task-based usability pilots
**Date**: **Saturday, November 29, 2025**
**Researcher**: **Oluwateniola Kuye**

---

## Purpose

Evaluate task manager usability and accessibility through structured pilots to gather empirical evidence for Week 10 redesign.

## Consent (UK GDPR/Data Protection Act 2018)

**Lawful basis**: Legitimate interest (educational research) + informed consent. **Data is strictly anonymised.**

**Before starting**, the Facilitator must read aloud and confirm:

> "Thanks for agreeing to pilot our prototype. This is a quick usability test—about 15 minutes. I'll ask you to complete 4 tasks while I observe and take notes. I'm testing the interface, not you, so there are no wrong answers.
>
> **What we're collecting (anonymously)**:
> - Task completion times (from server logs)
> - Success/failure on each task
> - Errors or validation issues
> - Your confidence ratings (1-5 scale)
> - My notes on any hesitations or accessibility issues
>
> **What we're NOT collecting**: Your name, email, student ID, or any recordings.
>
> I'll assign you a random session code (like `sid=X7kL9p`) which will appear in the logs. You can request that I delete all data linked to your session code at any time, even after today.
>
> **You can stop at any time**, no questions asked, and it won't affect your grade.
>
> Do you have any questions before we start?"

**Verbal consent**: "Are you happy to proceed?" Record verbal confirmation in the consent log.

---

## Procedure

### Setup (5 minutes)
1. **Assign Mode**: Alternate HTMX (JS-on: P1, P3, P5) and No-JS (JS-off: P2, P4).
2. **No-JS Setup**: Disable JavaScript in DevTools if assigned.
3. **Set Session ID**: Generate random 6-character ID (e.g., `sid=X7kL9p`). Set cookie in participant browser: `document.cookie = "sid=X7kL9p; path=/";`
4. **Open Task Manager**: http://localhost:8080/tasks (pre-populated with seed data).

### Orientation (2 minutes)
- Explain **Think-Aloud** (optional but encouraged).
- Reiterate neutrality ("I won't help unless you're stuck for >3 minutes").

### Task Execution (12 minutes)
For each task (T1-T4, in order T3, T1, T2, T4):
1. **Facilitator** reads scenario aloud from `tasks.md`.
2. **Start timing** (server log starts on first action).
3. **Observer** notes: timestamps, quotes, errors, workarounds.
4. **Stop timing** when task complete or abandoned.
5. **Ask confidence**: "On a scale 1-5, how confident are you that you completed that task correctly?"
6. **Record**: Completion (1/0), confidence, notes.

### Debrief (3 minutes)
**Facilitator asks**:
1. "Which task felt most difficult?"
2. "Did anything surprise you or not work as expected?"
3. "Were there any points where you weren't sure if something had worked?"
4. "(For SR/keyboard users) Did you encounter any accessibility barriers?"

**Total time**: ~20 minutes per participant.

---

## Data Recording & Ethics Checklist

| Item | Status | Notes |
| :--- | :--- | :--- |
| **Data Source 1** | Automated: `data/metrics.csv` | Logs: time, error, mode, status code |
| **Data Source 2** | Manual: `pilot-notes.md` | Quotes, observations, confidence ratings |
| **PII Protection** | Pseudonyms/Session IDs only | `session_id` is anonymous, no real names used. |
| **Opt-Out Plan** | Defined | Deletion of all rows linked to `session_id` upon request. |
| **Ownership** | **Oluwateniola Kuye** | Researcher owns local data until module submission is complete. |

**Final Protocol Approved by**: **Oluwateniola Kuye**
**Date**: **Saturday, November 29, 2025**