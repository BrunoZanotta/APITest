# APITest

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

APITest is a Maven suite of API tests for the Postman Library API at `https://library-api.postmanlabs.com/`. The tests use REST Assured and TestNG to call the books endpoints and check status codes, JSON fields, and JSON Schemas.

The suite has a root health check, contract tests for creating and listing books, and functional tests for create, list, and delete, including missing credentials and error responses.

## Tech stack

Versions are the ones declared in `pom.xml`.

| Component | Version | In this project |
| --- | --- | --- |
| Java | 21 | `maven-compiler-plugin` `source` and `target`. The GitHub Actions workflow uses Temurin 21. |
| REST Assured | 5.5.1 | HTTP calls and response extraction. |
| REST Assured JSON Schema Validator | 5.5.1 | `matchesJsonSchema` in the contract tests. |
| TestNG | 7.11.0 | Runner, `@Test`, data providers, and `@BeforeClass`. |
| Hamcrest | 3.0 (`test` scope) | Assertions. |
| JavaFaker | 1.0.2 | Generated book fields, a 500-character title, and a random UUID. |
| Allure TestNG | 2.21.0 | `AllureTestNg` listener on `BaseTest`. `@AllureId` on the add-book tests. `allure.version` is `2.21.0`. |
| Allure REST Assured | 2.21.0 | Declared dependency. The Java sources do not attach an `AllureRestAssured` filter. |
| ExtentReports | 5.1.2 | Declared dependency. The Java sources do not reference it. |
| Lombok | 1.18.30 | Annotation-processor path of `maven-compiler-plugin` only. `Books` uses `@Data`, `@Builder`, `@AllArgsConstructor`, and `@RequiredArgsConstructor`. Lombok is not a Maven dependency. |
| Allure Maven Plugin | 2.11.2 | `report` goal bound to the `verify` phase. Results directory: `${project.build.directory}/allure-results`. |
| Maven Surefire Plugin | 3.1.2 | Includes `**/*Test.java`. `testFailureIgnore` is `false`. |
| Maven Compiler Plugin | 3.13.0 | Java 21. |

Maven coordinates: `APITest:APITest:1.0-SNAPSHOT`.

## Project structure

```
.
├── .github/workflows/ci.yml
├── allurectl.yml
├── pom.xml
├── LICENSE
├── README.md
└── src
    ├── main
    │   ├── java/br/com/bruno/api
    │   │   ├── BaseTest.java
    │   │   ├── ConfigLoader.java
    │   │   ├── dataprovider
    │   │   │   ├── AddBooksDataProvider.java
    │   │   │   ├── DeleteBooksDataProvider.java
    │   │   │   └── GetBooksDataProvider.java
    │   │   └── objects
    │   │       └── Books.java
    │   └── resources
    │       └── environment.properties
    └── test
        ├── java/br/com/bruno/api
        │   ├── services
        │   │   └── BookService.java
        │   └── test
        │       ├── contract
        │       │   ├── ContractAddBooksTest.java
        │       │   └── ContractGetBooksTest.java
        │       ├── functional
        │       │   ├── FunctionalAddBooksTest.java
        │       │   ├── FunctionalDeleteBooksTest.java
        │       │   └── FunctionalGetBooksTest.java
        │       └── healthcheck
        │           └── HealthCheckTest.java
        └── resources/json_schemas
            ├── add_books_schema.json
            └── get_books_schema.json
```

- `BaseTest` sets `baseURI` to `https://library-api.postmanlabs.com/`, builds a JSON request specification with relaxed HTTPS validation, and enables Rest Assured logging when validation fails.
- `ConfigLoader` loads `src/main/resources/environment.properties`.
- `BookService` sends `POST /books`, `GET /books`, `GET /book`, and `DELETE /books/{id}`.
- `Books` maps `id`, `title`, `author`, `genre`, `yearPublished`, `checkedOut`, `isPermanentCollection`, and `createdAt`.
- `AddBooksDataProvider` supplies a valid book, a null title, a single-space title, a 500-character title, and the raw body `{}`.
- `DeleteBooksDataProvider` supplies a random UUID.
- `GetBooksDataProvider` supplies `genre` `fiction` and `checkedOut` `false`. No test class references it.
- `add_books_schema.json` describes one book object. `get_books_schema.json` describes an array of book objects. Both require `id`, `title`, `author`, `genre`, `yearPublished`, `checkedOut`, `isPermanentCollection`, and `createdAt`.

## Setup

JDK 21 and Maven are required. This repository has no Maven Wrapper.

```bash
git clone https://github.com/BrunoZanotta/APITest.git
cd APITest
```

The project is a Maven project (`pom.xml`). Open that file in IntelliJ IDEA, Eclipse, or VS Code.

Create and delete requests send an `api-key` header. `BookService` reads it with `ConfigLoader.getProperty("api-key")` from `src/main/resources/environment.properties`. That file is already in the repository and defines the `api-key` property. The health check and the GET calls in `BookService` do not set this header.

`ConfigLoader` throws `Failed to load the configuration file.` when that file cannot be read. The class is loaded only when a call uses it.

Install dependencies without running tests (the same command the workflow uses):

```bash
mvn clean install -DskipTests
```

## Running the tests

From the repository root, run every class that matches `**/*Test.java`:

```bash
mvn test
```

Run one class:

```bash
mvn -Dtest=FunctionalDeleteBooksTest test
```

Test classes:

- `HealthCheckTest` — `GET` on the base URL, expects `200`.
- `ContractAddBooksTest` — `POST /books` with a generated book, expects `201`, and checks `add_books_schema.json`.
- `ContractGetBooksTest` — `GET /books`, expects `200`, checks `get_books_schema.json`, and asserts a non-empty list whose items have `id` and `title`.
- `FunctionalAddBooksTest` — eight `POST /books` cases, with `@AllureId` values `"7"` through `"14"`:
  - valid book, expects `201`, and compares `title`, `author`, `genre`, and `yearPublished`;
  - request without the `api-key` header, expects `401` and the message `Invalid or missing credentials. Make sure you add 'api-key' to the request headers with a valid value`;
  - created `id` is not null or blank;
  - two creates return different `id` values;
  - null title, expects `400` and `body/title must NOT have fewer than 1 characters`;
  - 500-character title, expects `500`, code `22001`, and a message that the value is too long for `character varying(255)`;
  - single-space title, expects `500` and `Cannot read properties of null (reading '0')`;
  - raw body `{}`, expects `400` and required-property errors for `title`, `author`, `genre`, and `yearPublished`.
- `FunctionalDeleteBooksTest` — creates a book and `DELETE /books/{id}` expects `204`; deleting a random UUID expects `404` and `Book with id '<id>' not found`.
- `FunctionalGetBooksTest` — `GET /books` expects `200` and compares the first item with the deserialized `Books` object; `GET /book` expects `404`, error `Not Found`, and message `Route GET:/book not found`.

`mvn clean install` runs the tests and, on the `verify` phase, the Allure `report` goal. A failing test fails the build because `testFailureIgnore` is `false`.

Surefire writes `target/surefire-reports`. Allure results are written to `target/allure-results`.

## Allure

`BaseTest` registers `io.qameta.allure.testng.AllureTestNg`. `FunctionalAddBooksTest` is the only class that sets `@AllureId`.

The Allure Maven Plugin `2.11.2` binds `report` to `verify` and reads `target/allure-results`. After a run that produced those results, open the report with:

```bash
mvn allure:serve
```

`mvn verify` or `mvn clean install` also runs the bound `report` goal.

`allurectl.yml` in the repository root contains:

- `outputDirectory`: `target/allure-results`
- result name: `API Test Run`
- framework: `allure-testng`
- `resultsDirectory`: `target/allure-results`
- `reportUrl`: `https://api.testops.cloud/project/1/test-cases`
- `environment`: `staging`
- label `service`: `books-api`

The workflow below does not pass this file to `allurectl`. It sends the endpoint, project id, results path, launch name, and token as command-line flags.

## Continuous integration

`.github/workflows/ci.yml` is named **Run API Tests and Upload to Allure TestOps**. It runs on `pull_request` events whose target branch is `main`.

The job `test` (**Test Maven**) uses `ubuntu-latest` and does the following:

1. Checks out the repository with `actions/checkout@v3`.
2. Sets up Temurin JDK 21 and Maven cache with `actions/setup-java@v3`.
3. Runs `mvn clean install -DskipTests`.
4. Runs `mvn test`.
5. Always uploads `target/allure-results` as the artifact `allure-results` with `actions/upload-artifact@v4`.
6. Runs `file allurectl`.
7. Always downloads `allurectl-linux` from the latest allurectl GitHub release, installs it on `/usr/local/bin`, and runs:

```bash
allurectl upload \
  --endpoint=https://api.testops.cloud \
  --project-id=1 \
  --results=target/allure-results \
  --launch-name="executed API GitHub" \
  --token $ALLURE_TOKEN
```

`ALLURE_TOKEN` is the repository secret `ALLURE_TOKEN`.

## Highlights

- Shared base URL `https://library-api.postmanlabs.com/` and one JSON request specification for the suite.
- HTTP calls are in `BookService`. Test classes assert status codes, JSON fields, and schemas.
- Contract tests check `POST /books` against an object schema and `GET /books` against an array schema.
- Functional tests assert `200`, `201`, `204`, `400`, `401`, `404`, and `500` responses, including the missing `api-key` header and a title longer than the API's `varchar(255)` column.
- TestNG data providers and JavaFaker `1.0.2` build the book payloads. Publication years are generated between 1950 and 2023.
- Allure results come from the TestNG listener. The Maven plugin can serve or generate the report locally. Pull-request CI uploads `target/allure-results` and sends a launch named `executed API GitHub` to Allure TestOps project `1`.

## License

MIT License. Copyright (c) 2025 BrunoZanotta. See [LICENSE](LICENSE).
