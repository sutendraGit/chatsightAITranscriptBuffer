package com.tfsc.ilabs.LiveChatBufferService.model;

import lombok.Data;

@Data
public class TranscriptBufferRequest {
    private String messageId;
    private String messageContent;
    private String sendTime;
}
