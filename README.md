# Spring Boot 4 Sample

A sample CRUD REST API application built with Spring Boot 4, MySQL 9.5.0, and Java 25.

## Features

- RESTful API with CRUD operations for Products
- Spring Boot 4 native API versioning using `version` attribute
- MySQL 9.5.0 database with Docker Compose integration
- JPA/Hibernate for data persistence
- Bean validation
- Global exception handling
- Pagination support
- Spring Boot Actuator for monitoring
- Integration tests with Testcontainers

## Tech Stack

- **Java**: 25
- **Spring Boot**: 4.0.0
- **Database**: MySQL 9.5.0
- **Build Tool**: Maven
- **Testing**: JUnit 5, Testcontainers

## API Versioning (Spring Boot 4 Feature)

This project demonstrates the new first-class API versioning introduced in Spring Framework 7 / Spring Boot 4.

### Configuration

API versioning is configured in `WebConfig.java` using `ApiVersionConfigurer`:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
            .usePathSegment(0)
            .addSupportedVersions("1.0", "2.0")
            .setDefaultVersion("1.0");
    }
}
```

### Controller Usage

Controllers use the `version` attribute on mapping annotations:

```java
@GetMapping(version = "1.0")
public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
    // ...
}

@GetMapping(path = "/{id}", version = "1.0")
public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
    // ...
}
```

### Versioning Strategies

Spring Boot 4 supports multiple versioning strategies:

| Strategy     | Configuration                       | Example URL/Header           |
|--------------|-------------------------------------|------------------------------|
| Path-based   | `usePathSegment(0)`                 | `/api/v1.0/products`         |
| Header-based | `useRequestHeader("X-API-Version")` | Header: `X-API-Version: 1.0` |
| Query param  | `useRequestParameter("version")`    | `/api/products?version=1.0`  |

## Prerequisites

- Java 25+
- Docker and Docker Compose
- Maven 3.9+

## Getting Started

### Clone the repository

```bash
git clone https://github.com/hendisantika/spring-boot4-sample.git
cd spring-boot4-sample
```

### Run the application

The application uses Spring Boot Docker Compose support, which automatically starts the MySQL container.

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` with context path `/api`.

### Database Configuration

MySQL runs on port **3310** (mapped from container's 3306).

| Property | Value           |
|----------|-----------------|
| Host     | localhost       |
| Port     | 3310            |
| Database | spring_boot4_db |
| Username | yu71            |
| Password | 53cret          |

## API Endpoints

Base URL: `http://localhost:8080/api/v1.0`

### Products API

| Method | Endpoint                                  | Description                  |
|--------|-------------------------------------------|------------------------------|
| POST   | `/v1.0/products`                          | Create a new product         |
| GET    | `/v1.0/products`                          | Get all products             |
| GET    | `/v1.0/products/{id}`                     | Get product by ID            |
| GET    | `/v1.0/products/paged`                    | Get products with pagination |
| GET    | `/v1.0/products/active`                   | Get all active products      |
| GET    | `/v1.0/products/category/{category}`      | Get products by category     |
| GET    | `/v1.0/products/search?keyword={keyword}` | Search products              |
| PUT    | `/v1.0/products/{id}`                     | Update a product             |
| DELETE | `/v1.0/products/{id}`                     | Delete a product             |

### Example Requests

**Create Product:**

```bash
curl -X POST http://localhost:8080/api/v1.0/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest Apple smartphone",
    "price": 999.99,
    "quantity": 100,
    "category": "Electronics",
    "isActive": true
  }'
```

**Get All Products:**

```bash
curl http://localhost:8080/api/v1.0/products
```

**Get Products with Pagination:**

```bash
curl "http://localhost:8080/api/v1.0/products/paged?page=0&size=10&sortBy=name&sortDir=asc"
```

**Search Products:**

```bash
curl "http://localhost:8080/api/v1.0/products/search?keyword=phone"
```

## Actuator Endpoints

| Endpoint             | Description               |
|----------------------|---------------------------|
| `/actuator/health`   | Application health status |
| `/actuator/info`     | Application information   |
| `/actuator/metrics`  | Application metrics       |
| `/actuator/env`      | Environment properties    |
| `/actuator/beans`    | Spring beans              |
| `/actuator/mappings` | Request mappings          |

## Running Tests

```bash
./mvnw test
```

Integration tests use Testcontainers to spin up a MySQL 9.5.0 container automatically.

## Project Structure

```
src/
├── main/
│   ├── java/id/my/hendisantika/springboot4sample/
│   │   ├── config/         # API versioning configuration
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Custom exceptions & handlers
│   │   ├── repository/     # JPA repositories
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
├── test/
│   └── java/               # Integration tests
└── compose.yaml            # Docker Compose configuration
```

## References

- [API Versioning in Spring](https://spring.io/blog/2025/09/16/api-versioning-in-spring/)
- [Spring Boot 4 & Spring Framework 7 - Baeldung](https://www.baeldung.com/spring-boot-4-spring-framework-7)

## License

This project is licensed under the MIT License.

## Author

**Hendi Santika**

- Email: hendisantika@yahoo.co.id
- Telegram: @hendisantika34
- Website: [s.id/hendisantika](https://s.id/hendisantika)
