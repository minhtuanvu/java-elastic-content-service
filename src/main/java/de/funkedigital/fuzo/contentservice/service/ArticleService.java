package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.models.StateFields;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;
    private final Map<Event.ActionType, Function<Event, List<Content>>> transformerActionMap = new HashMap<>();

    private final StateFields stateFields = new StateFields("published", new StateFields("published"));

    ArticleService(ContentRepo contentRepo,
                   SaveContentFunction saveContentFunction,
                   DeleteContentFunction deleteContentFunction,
                   ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        transformerActionMap.put(Event.ActionType.CREATE, saveContentFunction);
        transformerActionMap.put(Event.ActionType.UPDATE, saveContentFunction);
        transformerActionMap.put(Event.ActionType.DELETE, deleteContentFunction);
        this.objectMapper = objectMapper;
    }

    public Mono<String> get(Long id) {
        return contentRepo.findById(id).filter(this::checkState).map(Content::getBody);
    }

    private boolean checkState(Content content) {
        try {
            return objectMapper.readValue(content.getBody(), StateFields.class).equals(stateFields);
        } catch (IOException e) {
            LOGGER.warn("Unable to parse content for state checking: {}", content.getId(), e);
            return false;
        }
    }


    List<Content> handleEvent(Event event) {
        Event.ActionType actionType = event.getActionType();
        return Optional.ofNullable(transformerActionMap.get(actionType))
                .map(m -> m.apply(event))
                .orElseThrow(() -> new UnsupportedOperationException(actionType.toString()));
    }


    public Flux<String> searchBy(ContentSearchRequest contentSearchRequest) {
        return contentRepo.search(contentSearchRequest, stateFields);

    }
}
