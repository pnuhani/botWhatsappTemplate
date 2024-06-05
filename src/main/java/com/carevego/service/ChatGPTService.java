package com.carevego.service;

import com.carevego.model.ChatGPTRequest;
import com.carevego.model.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ChatGPTService {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${openai.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.openai.com/v1/completions"; // Replace with your fine-tuned model ID if needed

    public String getChatGPTResponse(String userMessage)  {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        ChatGPTRequest request = new ChatGPTRequest(userMessage,150);

        HttpEntity<ChatGPTRequest> requestEntity = new HttpEntity<>(request, headers);

        ChatGPTResponse response = restTemplate.postForObject(apiUrl, requestEntity, ChatGPTResponse.class);

        return response.getChoices().get(0).getText().trim();
    }
}
