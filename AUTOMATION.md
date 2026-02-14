# Banking API Automation - Run Commands

## Prerequisites
- Java 17
- Maven Wrapper (mvnw.cmd) - included in project
- Node.js + npm
- Newman: `npm install`

## Run Automation

### 1. Single command (recommended)
```powershell
.\run-tests.ps1
```
Auto-detects JAVA_HOME, runs Maven tests.

### 2. TestNG + Newman (Maven Wrapper)
```powershell
npm install
.\mvnw.cmd clean test
```
- BaseTest starts WireMock on port 8080
- AccountTests runs Newman per API (Create, Get, Deposit, Withdraw, Close)
- Uses Postman collection + environment

### 3. Newman only (WireMock must run separately)
```bash
# Terminal 1
wiremock --port 8080 --root-dir wiremock --verbose

# Terminal 2
npm install
npm test
```
Or with WireMock JAR:
```bash
java -jar wiremock-standalone-3.3.1.jar --port 8080 --root-dir wiremock --verbose
```

### 4. PowerShell script (all-in-one)
```powershell
.\run-automation.ps1
```
Downloads WireMock JAR if needed, starts WireMock, runs Newman, stops WireMock.

### 5. Newman CLI direct
```bash
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli
```

### 6. Newman with HTML report
```bash
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export newman-report.html
```

## Base URL
http://localhost:8080/api
