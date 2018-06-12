package de.funkedigital.fuzo.contentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.service.EventService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
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
    private final EventService eventService;
    private final ObjectMapper objectMapper;

    QueueController(EventService eventService,
                    ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }


    @SqsListener(value = "${queueUrl}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handleNotificationMessage(String eventString) throws IOException {
        Event event = objectMapper.readValue(eventString, Event.class);
        LOGGER.info("Handling event: {}", event);
        eventService.handleEvent(event).subscribe(new Subscriber<Content>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Content content) {

            }

            @Override
            public void onComplete() {
                LOGGER.info("Event handled successfully");
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.error("Failed to handle event", t);
            }
        });
    }


}
