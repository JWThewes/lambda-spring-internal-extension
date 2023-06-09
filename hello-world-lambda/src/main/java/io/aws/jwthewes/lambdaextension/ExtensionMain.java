// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package io.aws.jwthewes.lambdaextension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.function.adapter.aws.AWSLambdaUtils;

/**
 * Entry point for external extension
 */
public class ExtensionMain implements Runnable {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSLambdaUtils.class);
    private final String extension;

    public ExtensionMain() {
        LOGGER.info("ExtensionMain constructor called");
        LOGGER.info("starting registration of extension");
        // Register the extension for "INVOKE" events
        extension = ExtensionClient.registerExtension();
        LOGGER.info("Extension registration complete, extensionID: " + extension);
        registerJvmShutdownHook();
    }

    private void registerJvmShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(ExtensionMain::handleShutDown));
    }

    @Override
    public void run() {
        LOGGER.info("entering event loop");
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                String response = ExtensionClient.getNext(extension);
                if (response != null && !response.isEmpty()) {
                    var eventJsonObject = objectMapper.readTree(response);
                    JsonNode eventTypeElement = eventJsonObject.get("eventType");

                    // Depending upon event type perform corresponding actions
                    if (eventTypeElement != null) {
                        final String eventType = eventTypeElement.textValue();
                        switch (eventType) {
                            case "INVOKE":
                                handleInvoke(response);
                                break;
                            case "SHUTDOWN":
                                handleShutDown();
                                break;
                            default:
                                LOGGER.error("Invalid event type received " + eventType);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while processing extension -" + e.getMessage(), e);
            }
        }
    }

    /**
     * Shutdown extension if we receive a shutdown event from lambda container
     */
    private static void handleShutDown() {
        System.out.println("Shutting down the extension: " + System.currentTimeMillis());
        do {
            try {
                LOGGER.info("Still alive..." + System.currentTimeMillis());
                Thread.sleep(5);
            } catch (InterruptedException e) {
                System.err.println("Error while sleeping + " + e.getMessage());
            }
        } while (true);

    }

    /**
     * Process payload
     *
     * @param payload event payload
     */
    public static void handleInvoke(String payload) {
        LOGGER.info("Handling invoke from extension " + payload);
    }

}
