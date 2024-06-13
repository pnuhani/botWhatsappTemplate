package com.carevego.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatGPTRequest {
    @JsonProperty("prompt")

    private String prompt;
    private int maxTokens;

    public ChatGPTRequest(String prompt, int maxTokens) {
        this.prompt = prompt;
        this.maxTokens = maxTokens;
    }

    // Getters and setters
}
