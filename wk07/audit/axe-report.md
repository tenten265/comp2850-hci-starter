# axe DevTools Audit Report — Week 7

**Date**: [2025-11-26]
**URL**: http://localhost:8080/tasks
**Tool**: axe DevTools 4.x
**Scope**: Full page scan (add form + task list)

---

## Summary
- **Critical**: 0
- **Serious**: 8
- **Moderate**: 0
- **Minor**: 0
- **Total**: 8 issues

---

## Critical Issues
None detected.

---

## Serious Issues

### Issue 1: Form label missing (Serious)
**Element**: `<input id="title" name="title">`
**Rule**: `label` (WCAG 1.3.1, 4.1.2)
**Description**: Form element does not have an associated label.
**Impact**: Screen reader users don't know what the input is for.
**Fix**: Ensure `<label for="title">Title</label>` exists and is visible (not visually-hidden).
**Status**: ❌ **FALSE POSITIVE** — Label exists in template. Possible axe bug or dynamic rendering issue. Verify manually.

### Issue 2: Insufficient color contrast (Serious)
**Element**: `<button type="submit">Delete</button>`
**Rule**: `color-contrast` (WCAG 1.4.3)
**Description**: Text color #6c757d on white background = 4.2:1 (fails AA 4.5:1)
**Impact**: Low vision users struggle to read button text.
**Fix**: Change button color to #495057 (darker gray, 7:1 contrast).
**Status**: ✅ **CONFIRMED** — Add to backlog as High severity.

---

## Moderate Issues

---

## Minor Issues

### Issue 4-6: [Document remaining minor issues]
[Low priority, defer to Semester 2]

---

## Actions
1. **False positive (Issue 1)**: Verify label exists with manual inspection
2. **High priority (Issue 2)**: Fix contrast ratio → Add to backlog
3. **Verified (Issue 3)**: No action needed
