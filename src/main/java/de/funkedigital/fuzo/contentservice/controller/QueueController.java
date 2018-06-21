package de.funkedigital.fuzo.contentservice.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.models.LastQueueResult;
import de.funkedigital.fuzo.contentservice.service.EventService;

import org.elasticsearch.common.collect.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@ConditionalOnProperty("consumer.enabled")
public class QueueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueController.class);
    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSQS;
    private final LastQueueResult lastQueueResult;
    private final String url;

    QueueController(EventService eventService,
                    ObjectMapper objectMapper,
                    AmazonSQS amazonSQS,
                    LastQueueResult lastQueueResult,
                    @Value("${queueUrl}") String url) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
        this.amazonSQS = amazonSQS;
        this.lastQueueResult = lastQueueResult;
        this.url = url;
    }

    @Scheduled(fixedDelay = 1000L)
    public void handleNotificationMessage() {
        LOGGER.debug("Polling for messages");
        amazonSQS.receiveMessage(url)
                .getMessages()
                .stream()
                .map(message -> {
                    try {
                        LOGGER.info("Handling message: {}", message);
                        return Tuple.tuple(message.getReceiptHandle(), objectMapper.readValue(message.getBody(), Event.class));
                    } catch (IOException e) {
                        LOGGER.error("Failed to read message", e);
                        lastQueueResult.setProcessedAt(LocalDateTime.now());
                        lastQueueResult.setMessage(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(tuple -> {
                    List<Content> result = eventService.handleEvent(tuple.v2());
                    LOGGER.info("Result: {}", result);
                    amazonSQS.deleteMessage(url, tuple.v1());
                    lastQueueResult.setProcessedAt(LocalDateTime.now());
                    lastQueueResult.setMessage("OK");
                });


    }


}
