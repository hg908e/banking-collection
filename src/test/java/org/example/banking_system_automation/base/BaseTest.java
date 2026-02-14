package org.example.banking_system_automation.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    protected static final String BASE_URI = "http://localhost:8080/api";

    protected static WireMockServer wireMockServer;

    @BeforeSuite
    public void beforeSuite() {
        log.info("Starting WireMock server on port 8080");
        Path wiremockPath = Paths.get("wiremock").toAbsolutePath();
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig()
                        .port(8080)
                        .usingFilesUnderDirectory(wiremockPath.toString())
        );
        wireMockServer.start();
        log.info("WireMock server started");
    }

    @AfterSuite
    public void afterSuite() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            log.info("Stopping WireMock server");
            wireMockServer.stop();
        }
    }
}
