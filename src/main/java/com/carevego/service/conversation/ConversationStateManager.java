package com.carevego.service.conversation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConversationStateManager {
    private Map<String, ConversationState> conversationStates = new HashMap<>();

    public ConversationState getState(String userId) {
        return conversationStates.computeIfAbsent(userId, k -> new ConversationState());
    }
}
