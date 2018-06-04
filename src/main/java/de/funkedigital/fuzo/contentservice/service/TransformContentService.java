package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.BiFunction;

import reactor.core.publisher.Mono;

/**
 * Created By {kazi}
 */
@Component
public class TransformContentService implements BiFunction<JsonNode, Long, Mono<Content>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformContentService.class);


    private static final String PAYLOAD_FIELD = "payload";

    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;

    public TransformContentService(ContentRepo contentRepo, ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Content> apply(JsonNode node, Long articleId) {
        Mono<Content> savedContent = null;

        try {
            savedContent = contentRepo.save(new Content(articleId,
                    objectMapper.writeValueAsString(node.get(PAYLOAD_FIELD))));
        } catch (IOException ex) {
            LOGGER.error("ERROR while transforming : ", ex);
        }
        return savedContent;

    }
}
