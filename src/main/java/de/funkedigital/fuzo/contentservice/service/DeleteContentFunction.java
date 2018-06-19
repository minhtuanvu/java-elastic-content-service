package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Service
public class DeleteContentFunction implements Function<Event, List<Content>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteContentFunction.class);

    private final ContentRepo contentRepo;

    DeleteContentFunction(ContentRepo contentRepo) {
        this.contentRepo = contentRepo;
    }

    @Override
    public List<Content> apply(Event event) {
        try {
            contentRepo.delete(event.getObjectId());
        } catch (IOException e) {
            LOGGER.error("Failed to delete content", e);
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }
}
