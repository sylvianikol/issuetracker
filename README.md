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

### Auth
| Methods   | Urls        | Actions       |
| --------- | ----------- | ------------- |
| POST      | /signin     | user sign in  | 
| POST      | /sign-up    | user sign up  |

### Defaults
| Methods   | Urls                                                       | Actions                         |
| --------- | ---------------------------------------------------------- | ------------------------------- |
| GET       | page=0&size=10                                             | retrieve all default page size  |
| GET       | @PageableDefault(sort = {"status", "priority","createdOn"} | retrieve all tasks default sort |
| GET       | @PageableDefault(sort = {"username"}                       | retrieve all users default sort |

### Tasks 
| Methods     | Urls                          | Actions                                     |
| ----------- | ------------------------------| ------------------------------------------- |
| GET         | /tasks                        | retrieve all tasks                          | 
| DELETE      | /tasks                        | delete all tasks                            |
| GET         | /tasks?userId={userId}        | retrieve all tasks by userId                |
| DELETE      | /tasks?userId={userId}        | delete all tasks by userId                  |
| GET         | /tasks?title={keyword}        | retrieve all tasks by title containing      |
| GET         | /tasks?page=0&sort={status}   | all tasks with page and sort                |


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
* Apache Maven
* Java Mail
* Spring Security
* Spring Boot Web
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

