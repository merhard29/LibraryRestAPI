# Library REST API Documentation

A simple Spring Boot REST web service for managing a library.
It provides CRUD operations for **Books**, **Categories** and **Authors**.

---

## Features

- CRUD operations for **Books**, **Categories**, and **Customers**
- **JWT authentication** (login endpoint returns token)
- **Public access** for reading books and categories
- **Authenticated access** for creating, updating, and deleting
- **Input validation** (`@NotBlank`, `@Email`)
- **Swagger UI** documentation for easy exploration
- **Integration tests** for all REST endpoints
- **Stateless Spring Security configuration**

---

## Endpoint documentation (Swagger)

After starting the application, access the Swagger UI at:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

There you can:

- Browse all available endpoints
- View request/response models
- Authenticate via JWT (`Authorize` button)
- Execute API requests directly in the browser
