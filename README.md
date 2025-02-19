# User Guide for Fiona
## Quick Start
1. Ensure you have Java 17 or above installed in your Computer.
2. Mac users: Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
3. Download the latest .jar file from [here](https://github.com/darrenchooji/ip/releases).

## Adding a `Todo`
Adds a todo task to the task list. 

Format: `todo NAME`

Examples:
- `todo Plan Valentine's day`
- `todo Apply for internships`

## Adding a `Deadline`
Adds a deadline task to the task list.

Format: `deadline NAME /by yyyy-MM-dd HHmm`

Examples: 
- `deadline CS2103T iP /by 2025-02-21 2359`
- `deadline CS3230 Assignment 4 /by 2025-02-23 2359`

## Adding a `Event`
Adds an event to the task list.

Format: `event NAME /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm`

Examples:
- `event Internship interview /from 2025-02-20 1400 /to 2025-02-20 1430`

## Listing every task: `list`
Shows a list of all your current tasks.

Format: `list`

## Mark a task as done: `mark`
Mark the specified task as done.

Format: `mark TASK_INDEX`

Example: 
- `mark 1`

## Delete a task: `delete`
Delete a specified task.

Format: `delete TASK_INDEX`

Example:
- `delete 1`

## Unmark a task as not done yet: `unmark`
Unmark the specified task as not done yet.

Format: `unmark TASK_INDEX`

Example:
- `unmark 1`

## Search for task: `find`
Search for tasks via keywords, date, or datetime.

Format:
- `find KEYWORD`
- `find yyyy-MM-dd`
- `find yyyy-MM-dd HHmm`

Examples:
- `find CS`
- `find 2025-02-19`
- `find 2025-02-19 1830`