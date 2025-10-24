package com.tfsc.ilabs.LiveChatBufferService.model;

import java.time.Instant;
import java.util.Objects;

public class Message implements Comparable<Message> {
    private String content;
    private String messageId;
    private Instant sendTime;
    private SenderType senderType;

    public Message(String content, String messageId, Instant sendTime, SenderType senderType) {
        this.content = content;
        this.messageId = messageId;
        this.sendTime = sendTime;
        this.senderType = senderType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Instant getSendTime() {
        return sendTime;
    }

    public void setSendTime(Instant sendTime) {
        this.sendTime = sendTime;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    @Override
    public int compareTo(Message other) {
        int cmp = this.sendTime.compareTo(other.sendTime);
        if (cmp == 0) {
            return this.messageId.compareTo(other.messageId);
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}

