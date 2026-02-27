# Multi-User Project and Task Management System (Backend)

## 📌 Overview

This is a backend-only Spring Boot application that manages:

- Users (ADMIN, MANAGER, DEVELOPER)
- Projects
- Tasks

The system enforces strict business rules such as:

- Only ADMIN can create projects
- Only MANAGER can assign/reassign tasks
- Developers can update only their task status
- Strict task status transition rules
- Soft delete for projects
- Optimistic locking for concurrent task updates

---

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Docker & Docker Compose
- Swagger (OpenAPI)

---

## 🗄 Database

MySQL 8 is used as the database.

Optimistic locking is implemented using `@Version` in the Task entity.

---

## 🔐 Business Rules

### User Roles

- ADMIN
- MANAGER
- DEVELOPER

### Project Rules

- Only ADMIN can create project
- Project uses soft delete (isActive flag)
- A user can belong to multiple projects

### Task Rules

- TODO → IN_PROGRESS
- IN_PROGRESS → BLOCKED or DONE
- BLOCKED → IN_PROGRESS
- DONE cannot be changed

- Only MANAGER can assign/reassign
- Task can be assigned only to project members
- DEVELOPER can update only their assigned task
- Optimistic locking prevents concurrent overwrite

---

## 🚀 How To Run (Using Docker)

### 1️⃣ Build the JAR

```bash
mvn clean package
```

### 2️⃣ Run with Docker Compose

```bash
docker compose up --build
```

### 3️⃣ Access Application

```
http://localhost:8080
```

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 📦 API Endpoints

### User APIs
- POST /api/users

### Project APIs
- POST /api/projects
- POST /api/projects/{projectId}/users
- PUT /api/projects/{projectId}/deactivate

### Task APIs
- POST /api/tasks
- PUT /api/tasks/{taskId}/reassign
- PUT /api/tasks/{taskId}/status
- GET /api/tasks/project/{projectId}
- GET /api/tasks/user/{userId}
- GET /api/tasks/status/{status}

---

## ⚙ Pagination & Sorting

All task listing APIs support:

- page
- size
- sortBy
- sortDir

---

## 👨‍💻 Author

Sathish Kumar Segu
