# Leaderboard Service 

A Spring Boot microservice that provides **user authentication** and a **score leaderboard API**.  
Built with: **Spring Boot**, **Spring Security (JWT)**, **Spring Data JPA**, and **PostgreSQL** (or H2 for local testing).

---
## Overview
This project implements a backend service for managing users and scores in a game leaderboard system.  
It demonstrates secure authentication, database persistence, and RESTful API development using Java Spring Boot.

---
##  Features

- User registration and login with BCrypt password encryption  
- JWT-based authentication for secure endpoints  
- Leaderboard API for submitting and fetching scores  
- Global exception handling for clean JSON error responses  
- PostgreSQL database integration (H2 for local testing)  
- DTO responses for clean output  

---

##  Quickstart (Local)
1. Clone the repo:
```bash
git clone https://github.com/kirpiim/leaderboard-services.git
cd leaderboard-services
```
### 1. Configure the database

**Option A – PostgreSQL (recommended)**  
Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/leaderboard_db
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
 ```

### Create the database (if not already created):
```sql
CREATE DATABASE leaderboard_db;
```



**Option B – H2 (in-memory, no setup required)**
Replace with:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

### 2. Run the app

With Maven Wrapper (preferred):
```bash
./mvnw spring-boot:run
```

**Option B – Run via IntelliJ:**  
Run `LeaderboardServiceApplication`.  
The server will start at [http://localhost:8080](http://localhost:8080).

## API Usage Examples

Use curl or Postman to try the endpoints:

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/auth/register \
   -H "Content-Type: application/json" \
   -d '{"username":"newuser","email":"new@example.com","password":"password"}'
```

**Example response:**
```json
{
"success": true,
"message": "User registered successfully",
"token": null
}
```
### 2. Login
```bash
curl -X POST http://localhost:8080/auth/login \
   -H "Content-Type: application/json" \
   -d '{"email":"new@example.com","password":"password"}'
```

**Example response:**
```json
{
"success": true,
"message": "Login successful",
"token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
### 3. Submit a score (requires JWT token)
```bash
curl -X POST "http://localhost:8080/leaderboard/scores?points=200" \
   -H "Authorization: Bearer <your-jwt-token>"
```

**Example response:**
```json
"Score saved successfully!"
```

### 4. Get leaderboard
```bash
curl -X GET "http://localhost:8080/leaderboard?limit=5" \
   -H "Authorization: Bearer <your-jwt-token>"
```

**Example response:**

```json
[
    {
    "username": "newuser",
    "points": 200,
    "timestamp": "2025-09-30T14:25:32.163055Z"
    }
]
```

## Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL / H2
- Maven


## Error Handling

This service includes a global exception handler using `@ControllerAdvice`.  
It ensures all errors return clean JSON responses instead of raw stack traces.  
Examples:

- Invalid credentials → `401 Unauthorized` with `{ "error": "Invalid credentials" }`
- Validation errors (e.g., blank username, invalid email) → `400 Bad Request` with field-specific messages
- General runtime issues → `400 Bad Request` with an error message

## Continuous Integration(CI)
Automated build and test pipeline using **GitHub Actions**.  
Every push and pull request triggers unit tests and code validation.

## What I learned
- Built a modular Spring Boot backend with controller–service–repository structure
- Implemented secure JWT authentication with BCrypt password hashing
- Integrated MySQL/PostgreSQL via Spring Data JPA
- Tested REST APIs using Postman
- Configured multi-environment setups through application.properties
- Applied centralized error handling with @ControllerAdvice
- Set up automated CI/CD with GitHub Actions
- Wrote clean, scalable code following best practices

## Future Improvements

- Unit tests with JUnit + Spring Boot Test.
- Docker Compose for DB + service.
