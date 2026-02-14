package org.example.banking_system_automation.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.banking_system_automation.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AccountTests extends BaseTest {

    private static final Logger log = LogManager.getLogger(AccountTests.class);

    @Test
    public void healthTest() {
        runNewmanAndAssert("Health");
    }

    @Test
    public void createAccountTest() {
        runNewmanAndAssert("Create Account");
    }

    @Test
    public void getAccountTest() {
        runNewmanAndAssert("Get Account");
    }

    @Test
    public void depositTest() {
        runNewmanAndAssert("Deposit");
    }

    @Test
    public void withdrawTest() {
        runNewmanAndAssert("Withdraw");
    }

    @Test
    public void closeAccountTest() {
        runNewmanAndAssert("Close Account");
    }

    private String findNodeDir() {
        String nodeHome = System.getenv("NODE_HOME");
        if (nodeHome != null && Files.exists(Paths.get(nodeHome))) return nodeHome;
        String[] checkPaths = {
                "C:\\Program Files\\nodejs",
                System.getenv("ProgramFiles") + "\\nodejs",
                System.getenv("LOCALAPPDATA") + "\\Programs\\node"
        };
        for (String dir : checkPaths) {
            if (dir == null || dir.isEmpty()) continue;
            Path npxCmd = Paths.get(dir, "npx.cmd");
            Path nodeExe = Paths.get(dir, "node.exe");
            if (Files.exists(npxCmd) || Files.exists(nodeExe)) return dir;
        }
        return null;
    }

    private void runNewmanAndAssert(String folder) {
        String basePath = Paths.get("").toAbsolutePath().toString();
        String collectionPath = Paths.get(basePath, "postman", "Banking-API.postman_collection.json").toString();
        String envPath = Paths.get(basePath, "postman", "local.postman_environment.json").toString();

        if (!new File(collectionPath).exists() || !new File(envPath).exists()) {
            Assert.fail("Postman collection or environment not found at: " + basePath);
        }

        List<String> cmd = new ArrayList<>();
        cmd.add("npx.cmd");
        cmd.add("newman");
        cmd.add("run");
        cmd.add(collectionPath);
        cmd.add("-e");
        cmd.add(envPath);
        cmd.add("--folder");
        cmd.add(folder);
        cmd.add("--reporters");
        cmd.add("cli");

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            pb.directory(new File(basePath));
            String nodeDir = findNodeDir();
            if (nodeDir != null) {
                Map<String, String> env = pb.environment();
                String path = env.getOrDefault("Path", env.get("PATH"));
                String newPath = nodeDir + File.pathSeparator + (path != null ? path : "");
                env.put("Path", newPath);
                env.put("PATH", newPath);
            }
            Process process = pb.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    log.info(line);
                }
            }

            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                Assert.fail("Newman execution timed out");
            }

            Assert.assertEquals(process.exitValue(), 0, "Newman tests failed:\n" + output);
        } catch (Exception e) {
            log.error("Newman execution error", e);
            Assert.fail("Newman execution failed: " + e.getMessage());
        }
    }
}
