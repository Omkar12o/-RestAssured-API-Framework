# RestAssured API Automation Framework

API test automation framework built with **RestAssured + TestNG + Maven**, testing the public [GoRest](https://gorest.co.in/) REST API (`/users` resource).

## Tech Stack
- Java 11
- RestAssured 5.4
- TestNG 7.10
- Maven
- Jackson (POJO serialization)
- GitHub Actions (CI)

## Project Structure
```
RestAssured-API-Framework/
├── src/test/java/api/
│   ├── base/BaseTest.java         # Shared RequestSpecification setup
│   ├── pojo/User.java             # Request/response POJO
│   ├── tests/UserCrudTests.java   # Full CRUD + negative test suite
│   ├── tests/PerformanceTests.java# Response-time assertions
│   └── utils/
│       ├── ConfigReader.java      # Reads config.properties
│       └── DataGenerator.java     # Random test data generator
├── src/test/resources/config.properties
├── testng.xml
├── pom.xml
└── .github/workflows/ci.yml
```

## What's Covered
- **Create (POST)** – valid user creation, invalid email (422), missing required field (422)
- **Read (GET)** – fetch by id, list with query param filter, non-existent id (404)
- **Update** – full update (PUT), partial update (PATCH)
- **Delete (DELETE)** – delete + verify subsequent GET returns 404
- **Performance** – response time assertion on list endpoint
- Status code, body, and field-level assertions using Hamcrest matchers

## Setup
1. Create a free account at [gorest.co.in](https://gorest.co.in/) and generate an access token.
2. Paste the token into `src/test/resources/config.properties`:
   ```
   access.token=YOUR_TOKEN_HERE
   ```

## Run Tests
```bash
mvn clean test
```
Reports are generated at `target/surefire-reports/`.

## CI
Every push/PR to `main` triggers GitHub Actions (`.github/workflows/ci.yml`), which injects the token from a repository secret `GOREST_ACCESS_TOKEN` and runs the full suite.

## Author
Omkar — QA Automation Engineer
