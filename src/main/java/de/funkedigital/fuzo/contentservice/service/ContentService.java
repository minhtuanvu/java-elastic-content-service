package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Validated
public class ContentService {

    private final ContentRepo contentRepo;
    private final SaveContentFunction saveContentFunction;
    private final DeleteContentFunction deleteContentFunction;
    private final Map<Event.ActionType, Function<Event, Mono<Content>>> transformerActionMap = new HashMap<>();

    ContentService(ContentRepo contentRepo,
                   SaveContentFunction saveContentFunction,
                   DeleteContentFunction deleteContentFunction) {
        this.contentRepo = contentRepo;
        this.saveContentFunction = saveContentFunction;
        this.deleteContentFunction = deleteContentFunction;
        constructTransformerAction();
    }

    private void constructTransformerAction() {
        transformerActionMap.put(Event.ActionType.CREATE, saveContentFunction);
        transformerActionMap.put(Event.ActionType.UPDATE, saveContentFunction);
        transformerActionMap.put(Event.ActionType.DELETE, deleteContentFunction);

    }

    public Mono<String> get(Long id) {
        return contentRepo.findById(id).map(Content::getBody);
    }

    public Mono<Content> handleEvent(@Valid Event event) {
        Event.ActionType actionType = event.getActionType();

        return Optional.ofNullable(transformerActionMap.get(actionType))
                .map(m -> m.apply(event))
                .orElseThrow(() -> new UnsupportedOperationException(actionType.toString()));
    }


    public Flux<String> searchBy(ContentSearchRequest contentSearchRequest) {
        return contentRepo.search(contentSearchRequest);

    }
}
