package com.tfsc.ilabs.LiveChatBufferService.controller;

import com.tfsc.ilabs.LiveChatBufferService.model.BufferResponse;
import com.tfsc.ilabs.LiveChatBufferService.model.FetchTranscriptResponse;
import com.tfsc.ilabs.LiveChatBufferService.model.SenderType;
import com.tfsc.ilabs.LiveChatBufferService.model.TranscriptBufferRequest;
import com.tfsc.ilabs.LiveChatBufferService.model.TranscriptBufferResponse;
import com.tfsc.ilabs.LiveChatBufferService.service.TranscriptBufferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients/{clientId}/accounts/{accountId}/interactions/{interactionId}")
@Slf4j
public class TranscriptBufferController {

    @Autowired
    TranscriptBufferService transcriptBufferService;

    @PostMapping("/fromVisitor/{visitorSessionId}")
    public BufferResponse<TranscriptBufferResponse> registerVisitorMessage(@RequestBody TranscriptBufferRequest transcriptBufferRequest,
                                                                           @PathVariable String interactionId,
                                                                           @PathVariable String visitorSessionId,
                                                                           @PathVariable String clientId,
                                                                           @PathVariable String accountId) {
        TranscriptBufferResponse transcriptBufferResponse = transcriptBufferService.buffer(interactionId,transcriptBufferRequest,clientId,accountId, SenderType.VISITOR);
        if (transcriptBufferResponse.getIsBuffered()) {
            log.info("Transcript buffered successfully for interactionId {} and messageId {} from visitor {} ",interactionId
                    ,transcriptBufferRequest.getMessageId()
                    ,visitorSessionId);
            return new BufferResponse<>(transcriptBufferResponse, HttpStatus.ACCEPTED);
        } else {
            log.error("Transcript buffer failed for interactionId {} and messageId {} from visitor {} ",interactionId
                    ,transcriptBufferRequest.getMessageId()
                    ,visitorSessionId);
            return new BufferResponse<>(transcriptBufferResponse,HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/fromAgent/{agentSessionId}")
    public BufferResponse<TranscriptBufferResponse> register(@RequestBody TranscriptBufferRequest transcriptBufferRequest,
                                                             @PathVariable String interactionId,
                                                             @PathVariable String agentSessionId,
                                                             @PathVariable String clientId,
                                                             @PathVariable String accountId) {
        TranscriptBufferResponse transcriptBufferResponse = transcriptBufferService.buffer(transcriptBufferRequest,clientId,accountId,interactionId);
        if (transcriptBufferResponse.getIsBuffered()) {
            log.info("Transcript buffered successfully for interactionId {} and messageId {} from agent {}",interactionId,
                    transcriptBufferRequest.getMessageId(),
                    agentSessionId);
            return new BufferResponse<>(transcriptBufferResponse, HttpStatus.ACCEPTED);
        } else {
            log.error("Transcript buffer failed for interactionId {} and messageId {} from agent {}",interactionId,
                    transcriptBufferRequest.getMessageId(),
                    agentSessionId);
            return new BufferResponse<>(transcriptBufferResponse,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/transcripts")
    public BufferResponse<FetchTranscriptResponse> fetchTranscript(@PathVariable String interactionId,
                                                                   @PathVariable String clientId,
                                                                   @PathVariable String accountId) {
        try {
            FetchTranscriptResponse fetchTranscriptResponse = transcriptBufferService.fetchTranscript(clientId, accountId, interactionId);
            log.info("Transcript fetched successfully for interactionId {}", interactionId);
            return new BufferResponse<>(fetchTranscriptResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to fetch transcript for interactionId {}: {}", interactionId, e.getMessage());
            return new BufferResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
