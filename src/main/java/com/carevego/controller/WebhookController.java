package com.carevego.controller;


import com.carevego.model.Change;
import com.carevego.model.Entry;
import com.carevego.model.Message;
import com.carevego.model.WebhookRequest;
import com.carevego.service.ChatGPTService;
import com.carevego.service.conversation.ConversationState;
import com.carevego.service.conversation.ConversationStateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carevego.service.ShopifyOrderService;
import com.carevego.service.WhatsAppService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping()
public class WebhookController {

    // Replace this with your actual verify token
    private static final String VERIFY_TOKEN = "1234";
    private final WhatsAppService whatsappService;
    private final ShopifyOrderService shopifyOrderService;
    private final ConversationStateManager conversationStateManager;
    private final ChatGPTService chatGPTService;


    @Autowired
    public WebhookController(WhatsAppService whatsappService, ShopifyOrderService shopifyOrderService, ConversationStateManager conversationStateManager, ChatGPTService chatGPTService) {
        this.whatsappService = whatsappService;
        this.shopifyOrderService = shopifyOrderService;
        this.conversationStateManager = conversationStateManager;
        this.chatGPTService = chatGPTService;


    }

    // Verification endpoint
    @GetMapping
    public String verifyWebhook(@RequestParam Map<String, String> requestParams) {
        String mode = requestParams.get("hub.mode");
        String token = requestParams.get("hub.verify_token");
        String challenge = requestParams.get("hub.challenge");

        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return challenge;
        } else {
            return "Verification failed";
        }
    }

    @GetMapping("/nuhani")
    public ResponseEntity<?> getData() {

        return ResponseEntity.ok("hello");
    }

    // Endpoint to receive messages
    @PostMapping
    public void receiveMessage(@RequestBody WebhookRequest webhookRequest) throws IOException {
        // Read the request body as a string
        System.out.println("Received message: " + webhookRequest);
        if (webhookRequest.getErrors() != null && !webhookRequest.getErrors().isEmpty()) {
            webhookRequest.getErrors().forEach(error -> System.err.println("Error: " + error));
            return;
        }

        //start
        for (Entry entry : webhookRequest.getEntry()) {
            for (Change change : entry.getChanges()) {
                // Check if messages are null or empty
                if (change.getValue().getMessages() != null) {
                    for (Message message : change.getValue().getMessages()) {
                        String senderId = message.getFrom();
                        String messageType = message.getType();

                        // Retrieve conversation state
                        ConversationState state = conversationStateManager.getState(senderId);
                        if (state.isStopMessages()) {
                            System.out.println("Stopped messages for user: " + senderId);
                            return;
                        }

                        // Handle different message types
                        if ("text".equals(messageType) && message.getText() != null) {
                            String messageText = message.getText().getBody();
                            String chatGptResponse;
                            CompletableFuture<String> chatGPTResponseFuture = chatGPTService.getChatGPTResponse(messageText);
                            chatGPTResponseFuture.thenAccept(response -> {

                                // Process the response here
                                System.out.println("ChatGPT Response: " + response);

                            }).exceptionally(ex -> {
                                // Handle any exceptions that might occur
                                ex.printStackTrace();
                                return null;

                            });
                            processTemplateMessage(senderId, messageText);
                        } else if ("button".equals(messageType) && message.getButton() != null) {
                            String buttonPayload = message.getButton().getPayload();
                            String buttonText = message.getButton().getText();
                            processButtonMessage(senderId, buttonPayload, buttonText);
                        } //to get shopify order status
                        else if ("interactive".equals(messageType) && message.getInteractive().getType().equals("nfm_reply")) {
                            String orderId = message.getInteractive().getNfmReply().getPropertyFromResponseJson("orderId").toString();
                            Optional<String> trackingUrl;
                            if (null != orderId) {
                                trackingUrl = shopifyOrderService.getTrackingUrl(orderId);
                                trackingUrl.ifPresentOrElse(
                                        url -> whatsappService.sendTextMessage(senderId, true, String.valueOf(trackingUrl)),
                                        () -> whatsappService.sendTextMessage(senderId, false, "")
                                );
                            } else {
                                trackingUrl = null;
                            }
                        } else {
                            System.out.println("Unhandled message type: " + messageType);
                        }
                    }
                } else {
                    System.out.println("No messages found in the change.");
                }
            }
        }
        //end
    }

    private void processTemplateMessage(String senderId, String messageText) {
        System.out.println("Text message from: " + senderId);
        System.out.println("Message text: " + messageText);
        // Determine scenario and dynamic parameters
        String scenario = determineScenario(messageText);  // Implement your own logic here
        if ("stop".equals(scenario) || "escalate".equals(scenario) || "manual".equals(scenario)) {
            conversationStateManager.getState(senderId).setStopMessages(true);
            whatsappService.sendTextMessage(senderId, false, "Your request has been escalated. A human representative will assist you shortly.");
        } else {
            Map<String, Object> dynamicParams = new HashMap<>();
            dynamicParams.put("name", "User");  // Example dynamic parameter
            // Send template message based on the scenario
            whatsappService.sendTemplateMessage(senderId, scenario, dynamicParams);
        }
    }

    private void processButtonMessage(String senderId, String buttonPayload, String buttonText) {
        System.out.println("Button message from: " + senderId);
        System.out.println("Button payload: " + buttonPayload);
        System.out.println("Button text: " + buttonText);

        // Handle button message
        // Example: Respond with a confirmation template
        Map<String, Object> dynamicParams = new HashMap<>();
        Map<String, String> urlLink = new HashMap<>();
        dynamicParams.put("button_response", buttonText);
        String scenario = determineScenario(buttonPayload);  // Implement your own logic here

        if ("stop".equals(scenario) || "escalate".equals(scenario) || "manual".equals(scenario)) {
            conversationStateManager.getState(senderId).setStopMessages(true);
            whatsappService.sendTextMessage(senderId, false, "Your request has been escalated. A human representative will assist you shortly.");
        } else {

            if (scenario.contains("cta_url")) {
                whatsappService.sendInteractiveMessage(senderId, scenario, dynamicParams);
            } else if (scenario.contains("checkdelivery")) {
                whatsappService.sendFlowMessage(senderId, scenario, dynamicParams);
            } else {
                whatsappService.sendTemplateMessage(senderId, scenario, dynamicParams);
            }
        }
    }

    private String determineScenario(String anyMessageText) {
        String messageText = anyMessageText.toLowerCase();

        if (messageText.contains("hey")) {
            return "welcome";
        } else if (messageText.contains("product enquiry")) {
            return "choosecategory";
        } else if (messageText.contains("delivery")) {
            return "checkdelivery";
        } else if (messageText.contains("insulin cooler case")) {
            return "cta_url";
        } else if (messageText.contains("stop") || messageText.contains("escalate") || messageText.contains("manual")) {
            return "stop";
        } else {
            return "stop";
        }
    }

}
