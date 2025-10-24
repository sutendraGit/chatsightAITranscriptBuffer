package com.tfsc.ilabs.LiveChatBufferService.service;

import com.tfsc.ilabs.LiveChatBufferService.configs.RedisConfig;
import com.tfsc.ilabs.LiveChatBufferService.model.FetchTranscriptResponse;
import com.tfsc.ilabs.LiveChatBufferService.model.Message;
import com.tfsc.ilabs.LiveChatBufferService.model.SenderType;
import com.tfsc.ilabs.LiveChatBufferService.model.TranscriptBufferRequest;
import com.tfsc.ilabs.LiveChatBufferService.model.TranscriptBufferResponse;
import com.tfsc.ilabs.LiveChatBufferService.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;

@Service
@Slf4j
public class TranscriptBufferService {

    @Autowired
    RedisTemplate redisTemplate;

    public TranscriptBufferResponse buffer(String interactionId, TranscriptBufferRequest request, String clientId, String accountId, SenderType senderType) {
        TranscriptBufferResponse transcriptBufferResponse = new TranscriptBufferResponse();
        transcriptBufferResponse.setMessageId(request.getMessageId());
        try {
            // Create Message object from TranscriptBufferRequest
            Message message = new Message(
                    request.getMessageContent(),
                    request.getMessageId(),
                    Instant.parse(request.getSendTime()),
                    senderType
            );

            // Use Redis Sorted Set (ZSet) to maintain sorted order by sendTime
            // Convert sendTime to score (epoch seconds) for sorting
            double score = message.getSendTime().getEpochSecond();

            // Serialize message object to JSON string
            String messageJson = ObjectMapperUtils.getObjectMapper().writeValueAsString(message);

            // Add to Redis sorted set with interactionId as key
            String key =  clientId+"-"+accountId+"-"+interactionId;
            redisTemplate.opsForZSet().add(key, messageJson, score);

            log.info("Message buffered successfully for interactionId: {}, messageId: {}",
                    interactionId, request.getMessageId());

            transcriptBufferResponse.setIsBuffered(true);
        } catch (Exception e) {
            transcriptBufferResponse.setIsBuffered(false);
            log.error("Failed to buffer message for interactionId: {}, error: {}",
                    interactionId, e.getMessage());
        }
        return transcriptBufferResponse;
    }


    public FetchTranscriptResponse fetchTranscript(String clientId, String accountId, String interactionId) {
        try {
            // Construct the same key used in buffer method
            String key = clientId + "-" + accountId + "-" + interactionId;

            // Fetch all messages from Redis sorted set in ascending order (by sendTime)
            Set<String> messageJsonSet = redisTemplate.opsForZSet().range(key, 0, -1);

            // Create TreeSet to store Message objects
            TreeSet<Message> messages = new TreeSet<>();

            // Deserialize each JSON string back to Message object
            for (String messageJson : messageJsonSet) {
                Message message = ObjectMapperUtils.getObjectMapper().readValue(messageJson, Message.class);
                messages.add(message);
            }

            log.info("Fetched {} messages for interactionId: {}", messages.size(), interactionId);

            return new FetchTranscriptResponse(interactionId, messages);

        } catch (Exception e) {
            log.error("Failed to fetch transcript for interactionId: {}, error: {}",
                    interactionId, e.getMessage());
            throw new RuntimeException("Failed to fetch transcript", e);
        }
    }
}
