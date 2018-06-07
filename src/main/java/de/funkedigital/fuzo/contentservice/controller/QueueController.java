package de.funkedigital.fuzo.contentservice.controller;

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

    QueueController(ContentService contentService) {
        this.contentService = contentService;
    }


    @SqsListener(value = "https://sqs.eu-central-1.amazonaws.com/656201843059/fuzo-content-queue",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handleNotificationMessage(String message) throws IOException {
        LOGGER.info("Receiving message with message {{}}", message);
        contentService.handleEvent(message).block();
    }


}
