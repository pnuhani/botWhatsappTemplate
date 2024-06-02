package com.carevego.controller;


import com.carevego.model.Change;
import com.carevego.model.Entry;
import com.carevego.model.Message;
import com.carevego.model.WebhookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carevego.service.ShopifyOrderService;
import com.carevego.service.WhatsAppService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping()
public class WebhookController {

    // Replace this with your actual verify token
    private static final String VERIFY_TOKEN = "1234";
    private final WhatsAppService whatsappService;
    private final ShopifyOrderService shopifyOrderService;

    @Autowired
    public WebhookController(WhatsAppService whatsappService , ShopifyOrderService shopifyOrderService) {
        this.whatsappService = whatsappService;
        this.shopifyOrderService = shopifyOrderService;

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

                        // Handle different message types
                        if ("text".equals(messageType) && message.getText() != null) {
                            String messageText = message.getText().getBody();
                            processTemplateMessage(senderId, messageText);
                        } else if ("button".equals(messageType) && message.getButton() != null) {
                            String buttonPayload = message.getButton().getPayload();
                            String buttonText = message.getButton().getText();
                            processButtonMessage(senderId, buttonPayload, buttonText);
                        } //to get shopify order status
                        else if ("interactive".equals(messageType) && message.getInteractive().getType().equals("nfm_reply")) {
                            String orderId = message.getInteractive().getNfmReply().getPropertyFromResponseJson("orderId").toString();
                            Optional<String> trackingUrl;
                            if(null != orderId){
                                 trackingUrl = shopifyOrderService.getTrackingUrl(orderId);
                                trackingUrl.ifPresentOrElse(
                                        url -> whatsappService.sendTextMessage(senderId, true , String.valueOf(trackingUrl)),
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
        Map<String, Object> dynamicParams = new HashMap<>();
        dynamicParams.put("name", "User");  // Example dynamic parameter
        // Send template message based on the scenario
        whatsappService.sendTemplateMessage(senderId, scenario, dynamicParams);
    }

    private void processButtonMessage(String senderId, String buttonPayload, String buttonText) {
        System.out.println("Button message from: " + senderId);
        System.out.println("Button payload: " + buttonPayload);
        System.out.println("Button text: " + buttonText);

        // Handle button message
        // Example: Respond with a confirmation template
        Map<String, Object> dynamicParams = new HashMap<>();
        Map<String,String> urlLink = new HashMap<>();
        dynamicParams.put("button_response", buttonText);
        String scenario = determineScenario(buttonPayload);  // Implement your own logic here

        if(scenario.contains("cta_url")){
            whatsappService.sendInteractiveMessage(senderId, scenario, dynamicParams);
        }else if (scenario.contains("checkdelivery")) {
            whatsappService.sendFlowMessage(senderId, scenario, dynamicParams);
        }else{
            whatsappService.sendTemplateMessage(senderId, scenario, dynamicParams);
        }
    }

    private String determineScenario(String messageText) {
        if (messageText.contains("Hey there")) {
            return "welcome";
        } else if (messageText.contains("Product Enquiry")) {
            return "choosecategory";
        } else if (messageText.contains("Delivery")) {
            return "checkdelivery";
        }
        else if (messageText.contains("Insulin cooler case")) {
            return "cta_url";
        }
        else {
            return "other";
        }
    }

}
