package com.carevego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NFMReply {
    private String responseJson;
    private String body;
    private String name;

    // Getters and Setters
    @JsonProperty("response_json")
    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPropertyFromResponseJson(String propertyName) {
        try {
            // Deserialize responseJson into JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseJson);

            // Get value of the specified property
            JsonNode propertyNode = rootNode.get(propertyName);
            if (propertyNode != null) {
                return objectMapper.treeToValue(propertyNode, Object.class);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return null; // Property not found or error occurred
    }
}
