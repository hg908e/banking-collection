# banking-api-automation

## Prerequisites
- Java 17
- Node.js (for Newman CLI)

## Run Automation

### Option 1: Single command (recommended)
```powershell
.\run-tests.ps1
```
Auto-detects JAVA_HOME if needed, runs Maven tests via wrapper.

### Option 2: Maven Wrapper directly
```powershell
npm install
.\mvnw.cmd clean test
```

### Option 3: Newman Only (WireMock must be running)
```bash
# Terminal 1 - Start WireMock
wiremock --port 8080 --root-dir wiremock --verbose

# Terminal 2 - Run Newman
npm install
npm test
```

### Option 4: PowerShell Script (starts WireMock, runs Newman)
```powershell
.\run-automation.ps1
```

### Option 5: Bash Script (Linux/Mac)
```bash
chmod +x run-automation.sh
./run-automation.sh
```

## Base URL
http://localhost:8080/api

## WireMock Automation
See **[WIREMOCK-AUTOMATION-GUIDE.md](WIREMOCK-AUTOMATION-GUIDE.md)** for step-by-step WireMock automation guide.
