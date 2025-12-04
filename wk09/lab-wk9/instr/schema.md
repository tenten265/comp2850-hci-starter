# Data Schema
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
- session_id: anonymous cookie (e.g., 6–8 chars)
- request_id: random per task attempt
- step: start/success/fail/validation_error/server_error
- ms: duration of the attempt (server-measured)

# Metrics CSV Schema — Week 9
 
**File**: `data/metrics.csv`  
 
**Purpose**: Standardize logging of participant interactions during the peer pilot study. Captures task events, outcomes, timing, and environment (JS/no-JS).  
 
---
 
## Columns
 
| Column       | Type         | Description | Example |

|--------------|-------------|-------------|---------|

| ts_iso       | ISO 8601 timestamp | Event timestamp in UTC | 2025-10-13T14:23:01.832Z |

| session_id   | String (6–12 chars) | Anonymous participant session ID | X7kL9p |

| request_id   | String | Unique request identifier for server log row | r001 |

| task_code    | String | Task identifier from evaluation plan | T1_filter, T2_edit, T3_add, T4_delete |

| step         | Enum | Event type | start, success, validation_error, fail, server_error |

| outcome      | String | Specific outcome or error code (empty if success) | blank_title, max_length |

| ms           | Integer | Duration from start to event in milliseconds | 1847 |

| http_status  | Integer | HTTP response code | 200, 400, 500 |

| js_mode      | Enum | JavaScript availability | on (HTMX), off (no-JS) |
 
---
 
## Example Rows
 
```csv

ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode

2025-10-13T14:23:01.832Z,X7kL9p,r001,T1_filter,success,,1847,200,on

2025-10-13T14:25:12.123Z,X7kL9p,r002,T3_add,validation_error,blank_title,234,400,on

2025-10-13T14:26:03.456Z,X7kL9p,r003,T3_add,success,,567,200,on

2025-10-13T14:28:15.789Z,X7kL9p,r004,T2_edit,success,,1234,200,on

 