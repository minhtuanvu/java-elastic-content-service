package de.funkedigital.fuzo.contentservice.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.service.EventService;

import org.elasticsearch.common.collect.Tuple;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty("consumer.enabled")
public class QueueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueController.class);
    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSQS;
    private final String url;

    QueueController(EventService eventService,
                    ObjectMapper objectMapper,
                    AmazonSQS amazonSQS,
                    @Value("${queueUrl}") String url) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
        this.amazonSQS = amazonSQS;
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
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(tuple -> {
                    Publisher<Content> result = eventService.handleEvent(tuple.v2());
                    if (result instanceof Mono) {
                        ((Mono<Content>) result).block();
                        amazonSQS.deleteMessage(url, tuple.v1());
                        LOGGER.info("Message removed from queue: {}", tuple.v1());
                    } else {
                        if (result instanceof Flux) {
                            ((Flux<Content>) result).blockLast();
                            amazonSQS.deleteMessage(url, tuple.v1());
                            LOGGER.info("Message removed from queue: {}", tuple.v1());
                        } else {
                            throw new RuntimeException("Unknown result object");
                        }
                    }

                });


    }


}
