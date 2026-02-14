#!/bin/bash
set -e
cd "$(dirname "$0")"

echo "Starting WireMock on port 8080..."
wiremock --port 8080 --root-dir wiremock --verbose &
WIREMOCK_PID=$!

sleep 3

trap "kill $WIREMOCK_PID 2>/dev/null || true" EXIT

echo "Running Newman API tests..."
npx newman run postman/Banking-API.postman_collection.json -e postman/local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export newman-report.html
