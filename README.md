# Spring Boot 4 Sample

[![Java CI with Maven](https://github.com/hendisantika/spring-boot4-sample/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot4-sample/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot)
![Dependabot](https://img.shields.io/badge/Dependabot-enabled-025E8C?logo=dependabot)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

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

- **Java**: 25 (LTS, enforced via `maven-enforcer-plugin`)
- **Spring Boot**: 4.1.2 (Spring Framework 7, Jackson 3)
- **Database**: MySQL 9.5.0
- **Build Tool**: Maven 3.9+ (wrapper included)
- **Testing**: JUnit 6, Testcontainers 2.x
- **CI/CD**: GitHub Actions + Dependabot

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

With path-segment versioning the version must be captured as a URI variable in the mapping
(here `/{version}/products`), and each handler declares the version it serves via the `version` attribute:

```java
@RestController
@RequestMapping("/{version}/products")
public class ProductController {
    // ...
}

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

- Java 25+ (e.g. `sdk env install` picks up the bundled `.sdkmanrc`)
- Docker and Docker Compose
- Maven 3.9+ (or use the bundled `./mvnw`)

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

Integration tests use Testcontainers 2.x (`testcontainers-mysql`, `testcontainers-junit-jupiter`) to spin up a
MySQL 9.5.0 container automatically, so a running Docker daemon is required.

## Continuous Integration

The [`Java CI with Maven`](.github/workflows/maven.yml) workflow runs on every push and pull request to `main`:

- Sets up Temurin JDK 25 with Maven dependency caching
- Runs `./mvnw -B -ntp verify` (compile, JDK enforcer check, unit and Testcontainers integration tests)
- Uploads Surefire test reports as a build artifact
- Submits the Maven dependency graph to GitHub on pushes to `main` for better Dependabot alerts
- Uses least-privilege permissions and cancels superseded runs

## Dependency Updates

[Dependabot](.github/dependabot.yml) keeps dependencies current (schedules in `Asia/Jakarta` time):

| Ecosystem      | Schedule     | Grouping                                                                 | Commit prefix   |
|----------------|--------------|--------------------------------------------------------------------------|-----------------|
| Maven          | Daily 05:00  | `spring-boot`, `testcontainers`, `maven-plugins`, other `minor-and-patch` | `build(deps)`   |
| GitHub Actions | Daily 05:00  | All actions in one PR                                                    | `ci(deps)`      |
| Docker Compose | Weekly (Mon) | MySQL image in `compose.yaml`                                            | `build(docker)` |

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
