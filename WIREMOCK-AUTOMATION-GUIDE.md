# WireMock Automation Guide

## Overview

This project uses **WireMock** as a mock API server and **Newman** (Postman CLI) for API automation.

```
WireMock (port 8080)  →  Mock Banking API
Newman                →  Runs Postman collection against WireMock
TestNG                →  Orchestrates WireMock + Newman
```

---

## Prerequisites

| Tool | Version | Check Command |
|------|---------|---------------|
| Java | 17+ | `java -version` |
| Node.js | Any LTS | `node -v` |
| npm | Any | `npm -v` |
| Maven | Via mvnw | `.\mvnw.cmd -v` |

---

## Step-by-Step: Run WireMock Automation

### Option A: Full Automation (Recommended)

```powershell
cd "d:\Automation\Banking system automation"
.\run-tests.ps1
```

**What happens:**
1. Detects JAVA_HOME
2. Installs Newman (`npm install`)
3. TestNG @BeforeSuite → starts WireMock on port 8080
4. TestNG runs 6 Newman tests (Health, Create, Get, Deposit, Withdraw, Close)
5. TestNG @AfterSuite → stops WireMock
6. Reports BUILD SUCCESS or FAILURE

---

### Option B: Newman + WireMock Script Only

```powershell
cd "d:\Automation\Banking system automation"
.\run-automation.ps1
```

**What happens:**
1. Downloads WireMock JAR if missing
2. Starts WireMock on port 8080
3. Runs full Newman collection
4. Generates `newman-report.html`
5. Stops WireMock

---

### Option C: Manual (2 Terminals)

**Terminal 1 – Start WireMock:**
```powershell
cd "d:\Automation\Banking system automation"

# If you have WireMock JAR
java -jar wiremock-standalone-3.3.1.jar --port 8080 --root-dir wiremock --verbose

# Or use run-automation.ps1 to download it first
```

**Terminal 2 – Run Newman:**
```powershell
cd "d:\Automation\Banking system automation"
npm install
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli
```

---

## WireMock Structure

```
wiremock/
├── mappings/           ← Request/Response definitions
│   ├── health.json
│   ├── create-account.json
│   ├── get-account.json
│   ├── deposit.json
│   ├── withdraw.json
│   └── close-account.json
└── __files/            ← Response body files
    ├── health-response.json
    ├── create-account-response.json
    ├── get-account-response.json
    ├── deposit-response.json
    ├── withdraw-response.json
    └── close-account-response.json
```

---

## Mapping Format

**Example: create-account.json**
```json
{
  "request": {
    "method": "POST",
    "url": "/api/accounts"
  },
  "response": {
    "status": 201,
    "bodyFileName": "create-account-response.json",
    "headers": {
      "Content-Type": "application/json"
    }
  }
}
```

**Example: __files/create-account-response.json**
```json
{
  "accountId": 1001,
  "customerName": "Rahul Sharma",
  "accountType": "SAVINGS",
  "balance": 5000,
  "status": "ACTIVE"
}
```

---

## Postman Collection Structure

| Folder | Request | Method | WireMock URL |
|--------|---------|--------|--------------|
| Health | Health | GET | /api/health |
| Create Account | Create Account | POST | /api/accounts |
| Get Account | Get Account | GET | /api/accounts/1001 |
| Deposit | Deposit | POST | /api/accounts/1001/deposit |
| Withdraw | Withdraw | POST | /api/accounts/1001/withdraw |
| Close Account | Close Account | DELETE | /api/accounts/1001 |

**Base URL:** `http://localhost:8080/api`

---

## Newman Commands

```powershell
# Run full collection
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli

# Run with HTML report
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export newman-report.html

# Run specific folder only
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --folder "Create Account"
```

---

## Troubleshooting

| Issue | Fix |
|-------|-----|
| Port 8080 in use | `netstat -ano \| findstr :8080` then `taskkill /PID <pid> /F` |
| JAVA_HOME not set | `$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"` |
| npx not found | Run `npm install` and ensure Node is in PATH |
| WireMock 404 | Ensure WireMock is running and mappings exist in `wiremock/mappings/` |

---

## Verification

After running `.\run-tests.ps1`, you should see:

```
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
