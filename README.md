# Project Title

SC technical exercise

## Description

Technical exercise implementing an example test on a dummy service deployment site.

## Getting Started

### Dependencies

### Core Testing Framework
- **Java 21** — Language version
- **Playwright 1.60.0** — Cross-browser browser automation
- **JUnit 5 (Jupiter) 5.14.4** — Test framework and runner
- **AssertJ 3.27.7** — Fluent assertions

### Reporting & Instrumentation
- **Allure 2.34.0** — Test reporting and analytics
- **AspectJ 1.9.25** — Bytecode weaver for Allure annotations

### Build Tools
- **Maven 3.6+** — Build and dependency management
- **Maven Failsafe 3.3.1** — Runs integration tests (acceptance tests)
- **Maven Compiler 3.13.0** — Compiles Java 21 source


### Installing

Clone from https://github.com/pastrypossum/sc-example-playwright-java

Execute tests and generate reports (see executing program section below)

### Executing program

Run the tests with
```
mvn clean verify
```

You can open `target/site/allure-maven-plugin/index.html` with intellij and let it serve the report.

Run the following command from the project directory to serve the reports without intellij:
```
mvn io.qameta.allure:allure-maven:serve
```


## Help

The test is expected to fail due to known issues with the dummy application.

```
Multiple Failures (2 failures)
-- failure 1 --Service name expected to be PaymentService but was paymentservice
-- failure 2 --Alerts enabled expected to be true but was false
```

