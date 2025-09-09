# Payment Wallet (Week 3 Task)

Java 8 + Spring Boot 2.7 + Spring Data JPA (H2). Includes:
- REST API with layered architecture
- Global exception handling
- HATEOAS links
- Static analysis (SonarLint-ready + sonar-project.properties)
- Mockito unit tests
- API docs via Springdoc OpenAPI (Swagger UI)
- Postman collection for manual testing

## How to run
```
mvn clean spring-boot:run
```
Navigate to Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:walletdb`, user: `sa`, password: empty)

## Base path
`/api/v1`

## Endpoints
- `POST /api/v1/users` — create user & wallet (201)
- `PATCH /api/v1/users/wallet` — add amount (200)
- `GET /api/v1/users/{userId}` — get balance (200)
- `POST /api/v1/users/wallet/transfer` — transfer funds (200)

> Note: Passwords are stored in plain text for demo; **do not** do this in production.

## SonarLint / SonarQube
- Install **SonarLint** plugin in your IDE and bind to this project.
- A sample `sonar-project.properties` is included if you want to run `mvn sonar:sonar` against a SonarQube/SonarCloud server.

## Tests
```
mvn -q -Dtest=com.example.wallet.service.UserServiceImplTest test
```
