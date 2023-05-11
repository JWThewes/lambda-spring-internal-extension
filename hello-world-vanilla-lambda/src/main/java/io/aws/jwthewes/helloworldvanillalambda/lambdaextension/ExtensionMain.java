// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package io.aws.jwthewes.helloworldvanillalambda.lambdaextension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Entry point for external extension
 */
public class ExtensionMain implements Runnable {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String extension;

    public ExtensionMain() {
        System.out.println("ExtensionMain constructor called");
        System.out.println("starting registration of extension");
        // Register the extension for "INVOKE" events
        extension = ExtensionClient.registerExtension();
        System.out.println("Extension registration complete, extensionID: " + extension);
        registerJvmShutdownHook();
    }

    private void registerJvmShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(ExtensionMain::handleShutDown));
    }

    @Override
    public void run() {
        System.out.println("entering event loop");
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
                                System.err.println("Invalid event type received " + eventType);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error while processing extension -" + e.getMessage());
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
        System.out.println("Handling invoke from extension " + payload);
    }

}
