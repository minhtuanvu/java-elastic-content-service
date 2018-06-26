package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created By {kazi}
 */
@Component
public class SaveContentFunction implements Function<Event, List<Content>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveContentFunction.class);

    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;
    private final UrlService urlService;

    public SaveContentFunction(ContentRepo contentRepo, ObjectMapper objectMapper, UrlService urlService) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
        this.urlService = urlService;
    }

    @Override
    public List<Content> apply(Event event) {
        try {
            String articleUrl = urlService.buildDefaultUrl(event.getPayload());
            event.getPayload().put("articleUrl", articleUrl);
            return Collections.singletonList(
                    contentRepo.save(
                            new Content(
                                    event.getObjectId(),
                                    objectMapper.writeValueAsString(event.getPayload())
                            )
                    )
            );
        } catch (IOException ex) {
            LOGGER.error("ERROR while transforming : ", ex);
            throw new RuntimeException(ex);
        }

    }
}
