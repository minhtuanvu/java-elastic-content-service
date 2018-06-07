package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

import reactor.core.publisher.Mono;

/**
 * Created By {kazi}
 */
@Component
public class SaveContentFunction implements Function<Event, Mono<Content>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveContentFunction.class);

    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;

    public SaveContentFunction(ContentRepo contentRepo, ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Content> apply(Event event) {

        try {
            return contentRepo.save(new Content(event.getObjectId(),
                    objectMapper.writeValueAsString(event.getPayload())));
        } catch (IOException ex) {
            LOGGER.error("ERROR while transforming : ", ex);
            throw new RuntimeException(ex);
        }

    }
}