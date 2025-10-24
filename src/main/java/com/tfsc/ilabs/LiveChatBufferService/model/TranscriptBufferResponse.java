package com.tfsc.ilabs.LiveChatBufferService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptBufferResponse {

        private Boolean isBuffered;
        private String messageId;

}
