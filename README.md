# SynTask
SynTask is an issue tracking system for managing bugs and planning tasks during a project development.

## Features
- Admin can view all tasks in the system.
- Admin can create new tasks.
- Admin can update and delete tasks.
- Admin can update user's details.
- Admin can delete users.
- Admin can manage user roles.
- Admin can assign users to tasks.
- User can add new tasks and is automatically assigned to them.
- User can update and delete their own tasks.
- Task has a priority (low, medium, high). 
- Task has a status (open, in progress, done).
- Email notification sent when user assigned to a task.
- Role-based access control
- Customizable task statuses and priorities
- In progress: In-app notifications

## REST API

### Authentication and Authorization 
- POST user sign in: /signin
- POST user sign up: /sign-up

### Defaults
- GET all default page size: page=0&size=10
- GET all tasks default sort: @PageableDefault(sort = {"status", "priority","createdOn"}
- GET all users default sort: @PageableDefault(sort = {"username"}

### Tasks 
- GET all tasks: /tasks 
- DELETE all tasks: /tasks
- GET all tasks by userId: /tasks?userId={userId}
- DELETE all tasks by userId: /tasks?userId={userId} 
- GET all tasks by title containing: /tasks?title={keyword}
- GET all tasks with page and sort: /tasks?page=0&sort={status}
- GET all tasks by userId with params: /tasks?userId={userId}&title={title}?page=0&sort={status}
- POST a new task: /tasks/add
- GET a task: /tasks/{taskId}
- POST edit a task: /tasks/{taskId} 
- DELETE a task: /tasks/{taskId} 
- POST assign user to task: /tasks/{taskId}/assign?userId={userId}

### Users (admin only)
- GET all users : /users
- GET all users by username containing: /users?username={username}
- GET all users with page and sort: /users?page=0&sort={status}
- GET all users with params: /users?username={username}?page=0&sort={status}
- DELETE all users : /users
- POST edit a user: /users/{userId}
- DELETE a user: /users/{userId}


## Setup

* Java 11
* Java Mail
* Spring Security
* Spring Web
* Spring Data JPA (Hibernate)
* Hibernate Validator
* Spring Boot Validation
* Spring Boot DevTools
* MySQL

## Integration Tests
* Spring Boot Test
* Spring Security Tests
* JUnit 5
* h2database

