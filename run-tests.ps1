$ErrorActionPreference = "Stop"

Write-Host "Starting WireMock on port 8080..."

$wiremock = Start-Process -FilePath "java" `
    -ArgumentList "-jar", "wiremock-standalone.jar", "--port", "8080", "--root-dir", "wiremock" `
    -PassThru -NoNewWindow

Start-Sleep -Seconds 3

try {
    Write-Host "Running Newman tests..."
    npx newman run postman/Banking-API.postman_collection.json `
        -e postman/local.postman_environment.json `
        --reporters cli,htmlextra `
        --reporter-htmlextra-export newman-report.html
}
finally {
    Write-Host "Stopping WireMock..."
    Stop-Process -Id $wiremock.Id -Force
}
