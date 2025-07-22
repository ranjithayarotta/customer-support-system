# 🛠️ Customer Support Ticket System

A Microservices-based system built with **Java 21** and **Spring Boot 3.x**, following **Hexagonal (Clean) Architecture** and the **CQRS** design pattern.

---

## 📐 Architecture

- Spring Boot 3.x, Java 21
- Spring Cloud Gateway (API Gateway)
- Hexagonal Architecture (Ports & Adapters)
- CQRS Pattern for Ticket Service
- H2 Database (for local testing; can be replaced with Oracle/Postgres)
- Spring Security + JWT for Authentication & Authorization

---

## 🔧 Microservices Overview

### 1. 🎫 Ticket Service

**Manages customer support tickets**

#### Responsibilities

**Command Side (Write):**
- Create ticket
- Update ticket
- Close ticket

**Query Side (Read):**
- Get tickets by ID
- Filter tickets by status/priority
- Get current user's tickets

#### Folder Structure


```
ticket-service/
├── config/        // Swagger, Beans, etc.
├── controller/    // Inbound Adapters (REST APIs)
├── domain/        // Domain Models & Events
├── entity/        // JPA Entities
├── mapper/        // DTO ↔ Entity Converters
├── repository/    // Outbound Adapters (Spring Data JPA)
└── service/       // Application Logic
```

#### APIs

| Method | Endpoint                      | Roles             | Description                    |
|--------|-------------------------------|-------------------|--------------------------------|
| POST   | /api/tickets/create           | CUSTOMER          | Create a new ticket            |
| PUT    | /api/tickets/{id}             | SUPPORT, ADMIN    | Update a ticket                |
| PATCH  | /api/tickets/{id}/close       | SUPPORT, ADMIN    | Close a ticket                 |
| GET    | /api/tickets                  | SUPPORT, ADMIN    | Filter tickets by status/priority |
| GET    | /api/tickets/mine             | CUSTOMER          | View own tickets               |
| GET    | /api/tickets/{id}             | ALL ROLES         | Get ticket by ID               |

**H2 Dev DB:**
- JDBC URL: `jdbc:h2:mem:ticketdb`
- User: `sa`, Password: `password`
- H2 Console: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
- Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

> ℹ️ Note: In-memory DB resets on restart. Replace with Oracle/Postgres in production.

---

### 2. 👤 User Service

**Handles user authentication, registration, and role-based access**

#### Responsibilities

- Register and authenticate users
- Generate and validate JWT tokens
- Admin-only user management

#### Folder Structure



H2 Dev DB:
JDBC URL: jdbc:h2:mem:ticketdb

User: sa, Password: password

H2 Console: http://localhost:8081/h2-console

Swagger UI: http://localhost:8081/swagger-ui/index.html

ℹ️ Note: In-memory DB resets on restart. Replace with Oracle/Postgres in production.

2. 👤 User Service
   Manages user authentication, registration, and role-based access.

Responsibilities:
Register and authenticate users

Generate and validate JWT tokens

Admin-only user management

Folder Structure:
```
user-service/
├── configuration/
├── controller/
├── domain/
├── entity/
├── mapper/
├── model/
├── repository/
└── service/
```

#### APIs

| Method | Endpoint             | Roles          | Description                   |
|--------|----------------------|----------------|-------------------------------|
| POST   | /api/users/register  | Public         | Register a new user           |
| POST   | /api/users/login     | Public         | Login and receive JWT         |
| GET    | /api/users/me        | Authenticated  | View current user's profile   |
| GET    | /api/users           | ADMIN          | List all users                |
| GET    | /api/users/{id}      | ADMIN          | View user by ID               |
| PUT    | /api/users/{id}      | ADMIN          | Update user                   |
| DELETE | /api/users/{id}      | ADMIN          | Delete user                   |

**H2 Dev DB:**
- JDBC URL: `jdbc:h2:mem:usersdb`
- User: `sa`, Password: `password`
- H2 Console: [http://localhost:8082/h2-console](http://localhost:8082/h2-console)
- Swagger UI: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

> ℹ️ Note: In-memory DB resets on restart. Replace with Oracle/Postgres in production.

---

### 3. 🚪 API Gateway

- Routes external requests to internal microservices
- Verifies JWT tokens via User Service
- Secured with Spring Security

---

## 🔐 JWT Authentication

- Token issued via `/api/users/login`
- Use token in `Authorization` header:



Authorization: Bearer <JWT>

---

## 🧪 Testing

- ✅ Unit Testing: JUnit 5, Mockito
- 🔄 Optional: Testcontainers (for containerized DB tests)

---

## 🧰 Tech Stack

| Layer        | Technology                           |
|--------------|--------------------------------------|
| Gateway      | Spring Cloud Gateway                 |
| Backend      | Spring Boot 3.x, Java 21             |
| Persistence  | H2 (dev), Oracle/Postgres (prod)     |
| Architecture | Hexagonal (Clean Architecture), CQRS |
| Security     | Spring Security, JWT                 |
| Build        | Maven                                |
| API Docs     | Swagger / OpenAPI                    |
| Monitoring   | Actuator                             |

---

## 🚀 Running Locally

### Prerequisites

- Java 20
- Maven
- H2

### Steps

```bash
# Clone the repository
git clone https://github.com/ranjithayarotta/customer-support-system
cd customer-support-system

# Start user-service
cd user-service
mvn spring-boot:run

# Start ticket-service
cd ticket-service
mvn spring-boot:run

# Start API gateway
cd api-gateway
mvn spring-boot:run
