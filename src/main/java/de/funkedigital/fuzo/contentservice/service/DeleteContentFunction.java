package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;

import java.util.function.Function;

import reactor.core.publisher.Mono;

@Service
public class DeleteContentFunction implements Function<Event, Mono<Content>> {

    private final ContentRepo contentRepo;

    DeleteContentFunction(ContentRepo contentRepo) {
        this.contentRepo = contentRepo;
    }

    @Override
    public Mono<Content> apply(Event event) {
        return contentRepo.delete(event.getObjectId());
    }
}
