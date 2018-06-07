package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContentService {

    private final ContentRepo contentRepo;
    private final SaveContentFunction saveContentFunction;
    private final Map<Event.ActionType, Function<Event, Mono<Content>>> transformerActionMap = new HashMap<>();

    ContentService(ContentRepo contentRepo,
                   SaveContentFunction saveContentFunction) {
        this.contentRepo = contentRepo;
        this.saveContentFunction = saveContentFunction;
        constructTransformerAction();
    }

    private void constructTransformerAction() {
        transformerActionMap.put(Event.ActionType.CREATE, saveContentFunction);
        transformerActionMap.put(Event.ActionType.UPDATE, saveContentFunction);

    }

    public Mono<String> get(Long id) {
        return contentRepo.findById(id).map(Content::getBody);
    }

    public Mono<Content> handleEvent(Event event) throws IOException {
        Event.ActionType actionType = event.getActionType();

        return Optional.ofNullable(transformerActionMap.get(actionType))
                .map(m -> m.apply(event))
                .orElseThrow(() -> new UnsupportedOperationException(actionType.toString()));
    }


    public Flux<String> searchBy(ContentSearchRequest contentSearchRequest) {
        return contentRepo.search(contentSearchRequest);

    }
}
