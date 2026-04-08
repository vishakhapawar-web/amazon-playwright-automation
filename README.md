# Amazon Automation – QA Assignment

## Tech Stack
- Java
- Playwright
- Maven
- JUnit
- Page Object Model (POM)

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
- Environment configurable test data
- Explicit waits
- Reusable components
- Clean project structure

## How to Run

```bash
mvn clean test
