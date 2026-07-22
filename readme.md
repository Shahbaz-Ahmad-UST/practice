# Final Assessment Automation

## Overview
This repository contains two automation projects:
- `UIAutomationFinalAssessment` — Playwright-based UI tests for the checkout flow, cart validation, and payment flow.
- `ApiAutomationFinalAssessment` — Maven/RestAssured API tests for user creation, token generation, and book listing.

---

## UI Automation (Playwright)

### Project location
- `UIAutomationFinalAssessment/`

### What it does
- loads application URLs from environment variables
- fills checkout and payment forms
- validates tax calculations and payment error handling
- generates HTML, blob, and list reports

### Important files
- `pages/` - page object models (`HomePage.ts`, `CartPage.ts`, `CheckoutPage.ts`, `PaymentPage.ts`, `ProductPage.ts`)
- `tests/` - test specs (`taxValidation.spec.ts`, `paymentProcessingErrorValidation.spec.ts`)
- `utils/env.ts` - environment loader for required values
- `playwright.config.ts` - Playwright configuration for browser, timeout, reporter, and CI behavior

### Setup
1. Open `UIAutomationFinalAssessment` folder.
2. Install dependencies:

cd "UIAutomationFinalAssessment"
npm install


### Environment variables
Create `UIAutomationFinalAssessment/.env` with the required values.
Example:
env
# Base application URL for UI tests
BASEURL=https://demoqa.com


> Note: `utils/env.ts` throws an error if any required value is missing.

### Run tests

cd "UIAutomationFinalAssessment"
npx playwright test


### Run a specific test
dcd "UIAutomationFinalAssessment"
npx playwright test tests/taxValidation.spec.ts


---

## API Automation (Maven + RestAssured)

### Project location
- `ApiAutomationFinalAssessment/`

### What it does
- creates a user with username and password
- generates authentication token
- retrieves the book list using the token

### Important files
- `pom.xml` - Maven build and dependency configuration
- `src/test/java/com/ust/finalAssessment/tests/DemoQATest.java` - JUnit test class
- `src/test/java/com/ust/finalAssessment/api/client/AuthClient.java` - API client for auth endpoints
- `src/test/java/com/ust/finalAssessment/api/client/BookClient.java` - API client for book endpoints
- `src/test/java/com/ust/finalAssessment/config/ApiCofiguration.java` - loads `BASE_URL` from `.env` or system env
- `src/test/java/com/ust/finalAssessment/factory/RequestSpecFactory.java` - RestAssured request specs

### Setup
1. Open `ApiAutomationFinalAssessment` folder.
2. Ensure Java 22 and Maven are installed.

### Environment variables
Create `ApiAutomationFinalAssessment/.env` or set system environment variables.
Example `.env`:
env
BASE_URL=https://demoqa.com
USER_NAME=user
USER_PASSWORD=*******
```

> Note: `ApiCofiguration` now loads `.env` if present and falls back to environment variables.

### Run tests
```bash
cd "ApiAutomationFinalAssessment"
mvn test
```

### Run a specific test class
```bash
cd "ApiAutomationFinalAssessment"
mvn -Dtest=DemoQATest test
```

---

## CI / GitHub Actions notes
- Make sure CI provides the required environment variables for both projects.
- Example secrets to configure in GitHub Actions:
  - `BASEURL`
  - `CUSTOMER_EMAIL`
  - `CUSTOMER_NAME`
  - `CUSTOMER_PHONE`
  - `CARD_NAME_ON_CARD`
  - `CARD_NUMBER`
  - `CARD_EXPIRY`
  - `CARD_CVV`
  - `BASE_URL`
  - `USER_NAME`
  - `USER_PASSWORD`

## Additional comments
- The UI project uses `dotenv` and expects the `.env` file in `UIAutomationFinalAssessment/`.
- The API project uses `java-dotenv` and may also read system env values in CI.
- If a value is missing, the test setup intentionally fails early with a descriptive error.
