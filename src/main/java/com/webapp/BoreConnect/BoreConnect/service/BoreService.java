package com.webapp.BoreConnect.BoreConnect.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class BoreService {
    private Process currentBoreProcess;
    private String currentUrl;

    public CompletableFuture<String> startBoreLocal(int port) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Kill any existing bore process
                if (currentBoreProcess != null && currentBoreProcess.isAlive()) {
                    currentBoreProcess.destroy();
                }

                ProcessBuilder processBuilder = new ProcessBuilder(
                        "bore", "local", String.valueOf(port),
                        "--to", "bore.pub"
                );

                processBuilder.redirectErrorStream(true);
                currentBoreProcess = processBuilder.start();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(currentBoreProcess.getInputStream())
                );

                String line;
                // Look for the line containing bore.pub
                while ((line = reader.readLine()) != null) {
                    if (line.contains("bore.pub:")) {
                        // Extract port number using regex
                        Pattern pattern = Pattern.compile("bore\\.pub:(\\d+)");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String remotePort = matcher.group(1);
                            currentUrl = "http://bore.pub:" + remotePort;

                            // Start background thread to continue reading output
                            startOutputReader(reader);

                            return currentUrl;
                        }
                    }
                }

                // Start background thread to continue reading output even if URL wasn't found
                startOutputReader(reader);

                return "Error: Could not extract URL from bore output";
            } catch (Exception e) {
                log.error("Error starting bore: ", e);
                return "Error: " + e.getMessage();
            }
        });
    }

    private void startOutputReader(final BufferedReader reader) {
        Thread outputThread = new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Log output for debugging purposes
                    log.debug("Bore output: {}", line);
                }
            } catch (Exception e) {
                log.error("Error reading bore output: ", e);
            }
        });
        outputThread.setDaemon(true);
        outputThread.start();
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void stopBore() {
        if (currentBoreProcess != null && currentBoreProcess.isAlive()) {
            currentBoreProcess.destroy();
            currentUrl = null;
        }
    }
}