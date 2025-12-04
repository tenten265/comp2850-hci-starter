# Metrics Definitions — Week 9
 
Reference: `../../references/evaluation-metrics-quickref.md`
 
---
 
## Objective Metrics
 
### 1. Completion Rate
**Definition**: Proportion of participants who successfully complete the task.
 
**Calculation**:  
**Data source**: Manual observation + server logs (`step=success`)
 
**Reporting**: Percentage per task (e.g., `T1: 4/5 = 80%`)
 
**Split by**:
- JS-on vs JS-off
- Keyboard-only vs mouse
- Screen reader vs visual
 
---
 
### 2. Time-on-Task
**Definition**: Duration from task start to completion or abandonment.
 
**Calculation**:  
- **Server-timed**: `ms` column in `metrics.csv` (start → success event)  
- **Backup**: Facilitator stopwatch (start when scenario read, stop when participant says "done")
 
**Reporting**:
- Median (primary)  
- MAD: Median absolute deviation  
- Range: Min–max for context
 
**Example**:  
T1 (Filter): Median: 24s, MAD: 6s, Range: 12s–58s (n=5)
 
**Split by**: JS-on vs JS-off
 
---
 
### 3. Error Rate
**Definition**: Proportion of attempts triggering validation errors.
 
**Calculation**:  
**Data source**: Manual observation + server logs (`step=success`)
 
**Reporting**: Percentage per task (e.g., `T1: 4/5 = 80%`)
 
**Split by**:
- JS-on vs JS-off
- Keyboard-only vs mouse
- Screen reader vs visual
 
---
 
### 2. Time-on-Task
**Definition**: Duration from task start to completion or abandonment.
 
**Calculation**:  
- **Server-timed**: `ms` column in `metrics.csv` (start → success event)  
- **Backup**: Facilitator stopwatch (start when scenario read, stop when participant says "done")
 
**Reporting**:
- Median (primary)  
- MAD: Median absolute deviation  
- Range: Min–max for context
 
**Example**:  
T1 (Filter): Median: 24s, MAD: 6s, Range: 12s–58s (n=5)
 
**Split by**: JS-on vs JS-off
 
---
 
### 3. Error Rate
**Definition**: Proportion of attempts triggering validation errors.
 
**Calculation**:  

**Data source**: `metrics.csv` (`step=validation_error`)
 
**Reporting**: Percentage per task + qualitative notes on error type
 
**Example**:  
T3 (Add Task): Error rate 2/5 = 40% (1× blank title, 1× exceeded max length)
 
---
 
### 4. Validation Error Count
**Definition**: Number of validation errors per participant per task.
 
**Calculation**: Count rows in `metrics.csv` with `step=validation_error` for given session + task
 
**Reporting**: Mean errors per task
 
**Example**:  
T2 (Edit): Mean errors per participant: 0.4 (2 errors across 5 participants)
 
---
 
## Subjective Metrics
 
### 5. Confidence Rating
**Definition**: Self-reported confidence that task was completed correctly.
 
**Scale**: 1 (not at all confident) → 5 (very confident)
 
**Collection method**: Ask immediately after each task:  
> "On a scale of 1 to 5, how confident are you that you completed that task correctly?"
 
**Reporting**:
- Mean ± standard deviation  
- Distribution counts (1–5)
 
**Example**:  
T1 (Filter): Mean confidence: 4.2 ± 0.8; Distribution: 0×1, 0×2, 1×3, 2×4, 2×5
 
---
 
### 6. Difficulty Rating (optional)
**Definition**: Perceived difficulty of task.
 
**Scale**: 1 (very easy) → 7 (very difficult)
 
**Collection method**: Post-task question:  
> "How difficult was that task?"
 
**Reporting**: Mean ± SD per task
 
---
 
### 7. Post-Session Satisfaction (optional)
**Method**: 2-question UMUX-Lite (if time permits):
1. "This system's capabilities meet my requirements" (1–7)  
2. "This system is easy to use" (1–7)
 
**Calculation**: Average of the two responses
 
**Reporting**: Mean score across all participants
 
---
 
## Qualitative Observations
 
### 8. Facilitator Notes
**Capture**:
- Hesitations  
- Verbalizations  
- Accessibility issues  
- Workarounds
 
**Format**: Timestamped notes in `pilot-notes.md`  
**Analysis**: Thematic coding
 
---
 
## Accessibility-Specific Metrics
 
### 9. Keyboard-Only Completion
**Definition**: Can task be completed using only keyboard?
 
**Measurement**: Binary per task (yes/no)
 
**Reporting**:  
"T1: Keyboard-accessible ✅" or "T3: Tab order broken ✗"
 
---
 
### 10. Screen Reader Announcement Quality
**Definition**: Are status messages and result counts announced appropriately?
 
**Measurement**: Qualitative note per task (announced / not announced / partial)
 
**Reporting**: List issues with WCAG references (4.1.3 Status Messages)
 
---
 
## Data Integrity Checks
Before analysis:
- Completeness: All tasks have `session_id`, `task_code`, `step`  
- Plausibility: Times within expected ranges (e.g., 12s–120s for T1)  
- Consistency: JS-mode matches observed condition  
- Outliers: Flag times >3× median for review
 
Document anomalies in `wk09/lab-wk9/research/data-notes.md`
 
---
 
## Summary Table
 
| Metric | Type | Source | Calculation | Reporting |
|--------|------|--------|-------------|-----------|
| Completion rate | Objective | Manual + logs | successes / attempts | % per task |
| Time-on-task | Objective | Server logs | Median + MAD | Seconds |
| Error rate | Objective | Server logs | errors / attempts | % per task |
| Validation error count | Objective | Server logs | Count per participant/task | Mean |
| Confidence | Subjective | Post-task question | Mean ± SD | 1–5 scale |
| Difficulty (optional) | Subjective | Post-task question | Mean ± SD | 1–7 scale |
| Post-session satisfaction (optional) | Subjective | UMUX-Lite | Average of 2 responses | 1–7 scale |
| Facilitator notes | Qualitative | Manual observation | Thematic coding | Categories |
| Keyboard-only completion | Accessibility | Manual test | Binary | ✅/✗ |
| Screen reader announcements | Accessibility | SR observation | Qualitative | Issues list |
 
---
 
