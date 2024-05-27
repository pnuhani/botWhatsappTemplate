package Model;

import Model.Change;
import Model.Entry;
import Model.Message;
import Model.WebhookRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping()
public class WebhookController {

    // Replace this with your actual verify token
    private static final String VERIFY_TOKEN = "1234";

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
    public void receiveMessage(@RequestBody WebhookRequest webhookRequest) {
        System.out.println("Received message: " + webhookRequest);

        // Extract and process the message
        for (Entry entry : webhookRequest.getEntry()) {
            for (Change change : entry.getChanges()) {
                for (Message message : change.getValue().getMessages()) {
                    String senderId = message.getFrom();
                    String messageType = message.getType();
                    String messageText = message.getText().getBody();

                    // Process the message
                    System.out.println("Message from: " + senderId);
                    System.out.println("Message type: " + messageType);
                    System.out.println("Message text: " + messageText);

                    // Here you can add your logic to handle the message, e.g., store it in a database,
                    // trigger a response, etc.
                }
            }
        }
    }
}
