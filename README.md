# Amazon Automation – QA Assignment

## Tech Stack
- Java 11
- Playwright
- Maven
- TestNG
- Page Object Model (POM)
- Allure (optional reporting dependency)

## Scenario Automated
1. Go to Amazon.in
2. Search for "HP smart tank"
3. Verify search results appear
4. Select printer "Smart Tank 589"
5. Verify product page opens
6. Select quantity = 2
7. Add to cart
8. Verify subtotal
9. Go to cart
10. Verify item name and quantity

## Automation Practices Used
- Page Object Model
- Environment-configurable URL, browser, and test data
- Explicit waits for dynamic UI
- Reusable page classes and utilities
- Maven project layout with TestNG suite (`testng.xml`)

## Configuration & Test Data
| File | Purpose |
|------|--------|
| `src/test/resources/config.properties` | Base URL, browser, headless, timeouts |
| `src/test/resources/testdata.json` | Search text, product name, quantity, assertions |
| `src/test/resources/testng.xml` | TestNG suite (which tests run) |

## Prerequisites
- JDK 11+
- Maven 3.6+
- Playwright browsers installed (first time only)

### Install Playwright browsers (first run)
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"