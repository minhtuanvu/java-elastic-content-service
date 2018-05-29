package de.funkedigital.fuzo.contentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/escenic-events")
@ConditionalOnProperty("consumer.enabled")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;

    NotificationController(ContentRepo contentRepo,
                           ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
    }

    @NotificationSubscriptionMapping
    public void handleSubscriptionMessage(NotificationStatus status) {
        status.confirmSubscription();
    }

    @NotificationMessageMapping
    public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) throws IOException {
        LOGGER.info("Receiving message with subject [{}] and message {{}}", subject, message);
        contentRepo.save(objectMapper.readValue(message, Content.class));
    }

    @NotificationUnsubscribeConfirmationMapping
    public void handleUnsubscribeMessage(NotificationStatus status) {
        status.confirmSubscription();
    }
}
