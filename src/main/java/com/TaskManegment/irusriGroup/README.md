Todo Management API
This is a Spring Boot-based RESTful API for managing Todo tasks. It includes user authentication with JWT, supports user registration and login, and provides CRUD operations for Todo items with advanced filtering and sorting capabilities.

#Table of Contents
Features
Tech Stack
Getting Started
Endpoints

#Features

User Authentication: Secure user registration and login using JWT.

Todo Management: CRUD operations for todo tasks.

Pagination and Sorting: Supports pagination and sorting for large datasets.

Filtering: Allows searching through todos based on various criteria.

Status Updates: Patch endpoint to update the completion status of a todo.

#Tech Stack
Java 17

Spring Boot 3.3.5

MySQL (JDBC & JPA)

Spring Security for authentication

JWT (JSON Web Token) for token-based security

Lombok for boilerplate reduction

MapStruct for object mapping

Swagger (OpenAPI) for API documentation

JUnit & Mockito for testing

#Getting Started

Prerequisites
Java 17
Maven 3.6+
MySQL database

#Endpoints

Authentication Endpoints
POST /api/auth/register: Register a new user
POST /api/auth/login: Authenticate user and receive JWT token

Todo Endpoints
POST /api/todos/save?userId={userId}: Create a new todo item

DELETE /api/todos/delete/{todoId}: Delete a todo item by ID

PUT /api/todos/update/{todoId}: Update a todo item by ID

GET /api/todos/all?pageNo={pageNo}&size={size}: Retrieve paginated list of todos

PATCH /api/todos/update/status/{id}?value={value}: Update completion status of a todo

GET /api/todos/search?searchText={text}: Search todos by text

GET /api/todos/sorted?pageNo={pageNo}&size={size}&sortBy={field}&sortDirection={asc/desc}: Retrieve sorted and paginated todos