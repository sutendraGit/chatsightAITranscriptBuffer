package com.tfsc.ilabs.LiveChatBufferService.model;

import java.time.Instant;
import java.util.Objects;
import java.util.TreeSet;

public class FetchTranscriptResponse {
    private String interactionId;
    private TreeSet<Message> messages = new TreeSet<>();

    public FetchTranscriptResponse(String interactionId, TreeSet<Message> messages) {
        this.interactionId = interactionId;
        this.messages = messages;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public TreeSet<Message> getMessages() {
        return messages;
    }

    public void setMessages(TreeSet<Message> messages) {
        this.messages = messages;
    }
}
