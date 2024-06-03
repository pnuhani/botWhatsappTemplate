package com.carevego.service.conversation;

public class ConversationState {
    private boolean stopMessages = false;

    public boolean isStopMessages() {
        return stopMessages;
    }

    public void setStopMessages(boolean stopMessages) {
        this.stopMessages = stopMessages;
    }
}
