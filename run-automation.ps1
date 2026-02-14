$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

function Test-JavaHome {
    param([string]$javaHomePath)
    if (-not $javaHomePath) { return $false }
    $javaExe = Join-Path $javaHomePath "bin\java.exe"
    return (Test-Path $javaExe)
}

function Find-JavaHome {
    $javaPath = (Get-Command java -ErrorAction SilentlyContinue).Path
    if ($javaPath) {
        $jdkPath = Split-Path (Split-Path $javaPath -Parent) -Parent
        if (Test-JavaHome $jdkPath) { return $jdkPath }
    }
    $paths = @(
        "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-17*",
        "C:\Program Files\Java\jdk-17*"
    )
    foreach ($p in $paths) {
        $found = Get-Item $p -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($found -and (Test-JavaHome $found.FullName)) { return $found.FullName }
    }
    return $null
}

if (-not (Test-JavaHome $env:JAVA_HOME)) {
    $jdk = Find-JavaHome
    if ($jdk) { $env:JAVA_HOME = $jdk; Write-Host "Using JAVA_HOME: $jdk" }
}

if (-not (Test-JavaHome $env:JAVA_HOME)) {
    Write-Host "ERROR: JAVA_HOME not set. Install JDK 17+."
    exit 1
}

Write-Host "Installing Newman if needed..."
cmd /c "npm install --no-fund --no-audit 2>nul"

Write-Host "Starting WireMock on port 8080..."
$wiremockJar = Join-Path $scriptDir "wiremock-standalone.jar"
if (-not (Test-Path $wiremockJar)) {
    Write-Host "Downloading WireMock standalone..."
    Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/wiremock/wiremock-standalone/3.3.1/wiremock-standalone-3.3.1.jar" -OutFile $wiremockJar -UseBasicParsing
}
$wiremock = Start-Process -FilePath "java" -ArgumentList "-jar", "wiremock-standalone.jar", "--port", "8080", "--root-dir", "wiremock", "--verbose" -PassThru -NoNewWindow -WorkingDirectory $scriptDir

Start-Sleep -Seconds 4

try {
    Write-Host "Running Newman API tests..."
    npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export newman-report.html
    $exitCode = $LASTEXITCODE
} finally {
    Write-Host "Stopping WireMock..."
    Stop-Process -Id $wiremock.Id -Force -ErrorAction SilentlyContinue
}

exit $exitCode
