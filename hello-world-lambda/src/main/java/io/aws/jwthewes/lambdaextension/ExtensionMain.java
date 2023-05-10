// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package io.aws.jwthewes.lambdaextension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Entry point for external extension
 */
public class ExtensionMain implements Runnable {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run() {
        // Register the extension for "INVOKE" and "SHUTDOWN" events
        final String extension = ExtensionClient.registerExtension();
        System.out.println("Extension registration complete, extensionID: " + extension);

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
                            case "INVOKE" -> handleInvoke(response);
                            case "SHUTDOWN" -> handleShutDown();
                            default -> System.err.println("Invalid event type received " + eventType);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error while processing extension -" + e.getMessage());
                e.printStackTrace();
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
                System.out.println("Still alive..." + System.currentTimeMillis());
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);

    }

    /**
     * Process payload
     *
     * @param payload event payload
     */
    public static void handleInvoke(String payload) {
        System.out.println("Handling invoke from extension " + payload);
    }

}
