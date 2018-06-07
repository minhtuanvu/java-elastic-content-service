package de.funkedigital.fuzo.contentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.service.ContentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnProperty("consumer.enabled")
public class QueueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueController.class);
    private final ContentService contentService;
    private final ObjectMapper objectMapper;

    QueueController(ContentService contentService,
                    ObjectMapper objectMapper) {
        this.contentService = contentService;
        this.objectMapper = objectMapper;
    }


    @SqsListener(value = "${queueUrl}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handleNotificationMessage(String eventString) throws IOException {
        Event event = objectMapper.readValue(eventString, Event.class);
        LOGGER.info("Handling event: {}", event);
        contentService.handleEvent(event).block();
    }


}
