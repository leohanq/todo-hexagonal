# Todo Hexagonal - Project Documentation

## Overview

A **Todo/Task management** REST API built with **Spring Boot 4** following **Hexagonal Architecture** (Ports & Adapters). The project demonstrates clean separation of concerns where the domain logic is completely isolated from infrastructure details like databases and web frameworks.

**Tech Stack:** Java 21, Spring Boot 4.0.8-SNAPSHOT, Spring Data JPA, H2 (in-memory), Lombok, Maven

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          INFRASTRUCTURE LAYER                                │
│                                                                             │
│  ┌───────────────────────────┐         ┌──────────────────────────────────┐ │
│  │    INBOUND ADAPTERS       │         │       OUTBOUND ADAPTERS          │ │
│  │    (Driving Side)         │         │       (Driven Side)              │ │
│  │                           │         │                                  │ │
│  │  ┌─────────────────────┐  │         │  ┌────────────────────────────┐  │ │
│  │  │  TaskController     │  │         │  │  JpaTaskRepositoryAdapter  │  │ │
│  │  │  POST /api/v1/tasks │  │         │  │  implements TaskRepository │  │ │
│  │  └────────┬────────────┘  │         │  └─────────────┬──────────────┘  │ │
│  │           │               │         │                │                 │ │
│  │  ┌────────┴────────────┐  │         │  ┌─────────────┴──────────────┐  │ │
│  │  │  CreateTaskRequest  │  │         │  │  TaskPersistenceMapper     │  │ │
│  │  │  TaskResponse (DTO) │  │         │  │  TaskJpaEntity             │  │ │
│  │  └─────────────────────┘  │         │  │  SpringDataTaskRepository  │  │ │
│  │                           │         │  └────────────────────────────┘  │ │
│  └───────────────┬───────────┘         └──────────────┬───────────────────┘ │
│                  │                                     │                     │
└──────────────────┼─────────────────────────────────────┼─────────────────────┘
                   │                                     │
         ┌─────────▼──────────┐               ┌─────────▼──────────┐
         │   INBOUND PORT     │               │   OUTBOUND PORT    │
         │  (Driving Port)    │               │  (Driven Port)     │
         │                    │               │                    │
         │ CreateTaskUseCase  │               │ TaskRepositoryPort │
         │ + create(Task)    │               │ + save(Task)       │
         └─────────┬──────────┘               └─────────▲──────────┘
                   │                                     │
┌──────────────────┼─────────────────────────────────────┼─────────────────────┐
│                  │        APPLICATION LAYER             │                     │
│                  │                                     │                     │
│                  │    ┌────────────────────────┐       │                     │
│                  └───►│     TaskService        ├───────┘                     │
│                       │ implements             │                             │
│                       │ CreateTaskUseCase      │                             │
│                       └────────────────────────┘                             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            DOMAIN LAYER                                      │
│                       (No framework dependencies)                            │
│                                                                             │
│    ┌──────────────────────┐    ┌────────────┐    ┌───────────────────────┐  │
│    │        Task          │    │ TaskStatus │    │ TaskNotFoundException │  │
│    │  - id: Long          │    │            │    │                       │  │
│    │  - title: String     │    │  PENDING   │    │ extends               │  │
│    │  - description: Str  │    │  COMPLETED │    │ RuntimeException      │  │
│    │  - status: TaskStatus│◄───┤            │    │                       │  │
│    │  - createdAt: LDT    │    └────────────┘    └───────────────────────┘  │
│    │  - completedAt: LDT  │                                                 │
│    │                      │                                                 │
│    │  + completed()       │                                                 │
│    │  + reopen()          │                                                 │
│    │  + initDefaults()    │                                                 │
│    └──────────────────────┘                                                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Request Flow Diagram

```
    Client (HTTP)
        │
        │  POST /api/v1/tasks
        │  { "title": "...", "description": "..." }
        │
        ▼
┌───────────────────┐
│  TaskController   │──── Validates @Valid CreateTaskRequest
│  (REST Adapter)   │──── Maps DTO → Domain Task
└───────┬───────────┘
        │
        ▼
┌───────────────────┐
│ CreateTaskUseCase │──── Inbound Port (interface)
│    (Port In)      │
└───────┬───────────┘
        │
        ▼
┌───────────────────┐
│   TaskService     │──── Application Service (orchestrates use case)
│  (Use Case Impl) │
└───────┬───────────┘
        │
        ▼
┌───────────────────┐
│TaskRepositoryPort │──── Outbound Port (interface)
│    (Port Out)     │
└───────┬───────────┘
        │
        ▼
┌───────────────────┐
│JpaTaskRepository  │──── Calls task.initDefaults()
│    Adapter        │──── Maps Domain → JPA Entity
│  (Persistence)    │──── Persists via SpringDataTaskRepository
└───────┬───────────┘──── Maps JPA Entity → Domain
        │
        ▼
┌───────────────────┐
│   H2 Database     │──── Table: tasks
│   (In-Memory)     │
└───────────────────┘
```

---

## Domain Model

```
┌──────────────────────────────────────┐
│              Task                     │
├──────────────────────────────────────┤
│  id           : Long          [PK]   │
│  title        : String     [NOT NULL]│
│  description  : String     [nullable]│
│  status       : TaskStatus [NOT NULL]│
│  createdAt    : LocalDateTime        │
│  completedAt  : LocalDateTime        │
├──────────────────────────────────────┤
│  + completed() : void                │
│  + reopen()    : void                │
│  + initDefaults() : void             │
└──────────────────────────────────────┘
            │
            │ uses
            ▼
┌──────────────────────┐
│     TaskStatus       │
├──────────────────────┤
│  PENDING             │
│  COMPLETED           │
└──────────────────────┘
```

### Domain Rules

| Method | Behavior |
|--------|----------|
| `completed()` | Sets status → COMPLETED, sets `completedAt`. Throws `IllegalStateException` if already COMPLETED |
| `reopen()` | Sets status → PENDING, clears `completedAt`. Throws `IllegalStateException` if already PENDING |
| `initDefaults()` | Sets status → PENDING and `createdAt` → now (if null) |

---

## Database Schema

```
┌──────────────────────────────────────────┐
│               tasks                       │
├──────────────┬───────────────────────────┤
│ Column       │ Type                      │
├──────────────┼───────────────────────────┤
│ id           │ BIGINT (PK, IDENTITY)     │
│ title        │ VARCHAR (NOT NULL)        │
│ description  │ VARCHAR (nullable)        │
│ status       │ VARCHAR (NOT NULL)        │
│ created_at   │ TIMESTAMP (NOT NULL)      │
│ completed_at │ TIMESTAMP (nullable)      │
└──────────────┴───────────────────────────┘
```

---

## Package Structure

```
src/main/java/com/detalles/todo_hexagonal/
├── TodoHexagonalApplication.java
├── domain/
│   ├── model/
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   └── exception/
│       └── TaskNotFoundException.java
├── application/
│   ├── port/
│   │   ├── in/
│   │   │   └── CreateTaskUseCase.java
│   │   └── out/
│   │       └── TaskRepositoryPort.java
│   └── service/
│       └── TaskService.java
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── rest/
        │       ├── TaskController.java
        │       └── dto/
        │           ├── CreateTaskRequest.java
        │           └── TaskResponse.java
        └── out/
            └── persistence/
                ├── TaskJpaEntity.java
                ├── SpringDataTaskRepository.java
                ├── JpaTaskRepositoryAdapter.java
                └── TaskPersistenceMapper.java
```

---

## REST API

| Method | Endpoint | Body | Response | Status |
|--------|----------|------|----------|--------|
| POST | `/api/v1/tasks` | `{ "title": "string", "description": "string" }` | `TaskResponse` | 201 Created |

### Example

**Request:**
```json
{
  "title": "Learn Hexagonal Architecture",
  "description": "Study ports and adapters pattern"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Learn Hexagonal Architecture",
  "description": "Study ports and adapters pattern",
  "status": "PENDING",
  "createdAt": "2026-08-05T10:30:00",
  "completedAt": null
}
```

---

## Dependency Rule

```
Infrastructure → Application → Domain
     ↓                ↓            ↓
  Knows about     Knows about   Knows about
  everything      domain only   nothing external

  Domain has ZERO framework dependencies (pure Java + Lombok)
```

The dependency rule ensures that:
- **Domain** is independent and testable without any framework
- **Application** depends only on domain, defines ports as interfaces
- **Infrastructure** implements ports and wires everything together via Spring DI

---

## Current State & Future Work

**Implemented:**
- Create Task (full end-to-end)

**Domain-ready but not wired:**
- Complete Task (`task.completed()`)
- Reopen Task (`task.reopen()`)
- Find Task (exception exists: `TaskNotFoundException`)
- Update / Delete Task