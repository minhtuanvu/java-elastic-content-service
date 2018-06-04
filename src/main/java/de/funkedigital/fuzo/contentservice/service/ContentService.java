package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.exceptions.RequiredFieldsException;
import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import reactor.core.publisher.Mono;

@Service
public class ContentService {

    private static final String ARTICLE_ID_FIELD = "objectId";
    private static final String ACTION_TYPE_FIELD = "actionType";
    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;
    private final TransformContentService transformContentService;
    private final Map<ActionType, BiFunction<JsonNode, Long, Mono<Content>>> transformerActionMap = new HashMap<>();

    public enum ActionType {
        CREATE, UPDATE
    }

    ContentService(ContentRepo contentRepo,
                   ObjectMapper objectMapper,
                   TransformContentService transformContentService
    ) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
        this.transformContentService = transformContentService;
    }

    @PostConstruct
    private void constructTransformerAction() {
        transformerActionMap.put(ActionType.CREATE, transformContentService);
        transformerActionMap.put(ActionType.UPDATE, transformContentService);

    }

    public Mono<String> get(Long id) {
        return contentRepo.findById(id).map(Content::getBody);
    }

    public Mono<Content> create(String contentString) throws IOException {
        JsonNode node = objectMapper.readTree(contentString);
        JsonNode articleIdNode = node.get(ARTICLE_ID_FIELD);
        if (articleIdNode != null && !StringUtils.isEmpty(articleIdNode.asText())) {
            JsonNode actionTypeNode = node.get(ACTION_TYPE_FIELD);
            if (actionTypeNode != null && !StringUtils.isEmpty(actionTypeNode.asText())) {
                ActionType actionType = ActionType.valueOf(actionTypeNode.asText().toUpperCase());

                return Optional.ofNullable(transformerActionMap.get(actionType))
                        .map(m -> m.apply(node, articleIdNode.asLong()))
                        .orElseThrow(() -> new UnsupportedOperationException(actionType.toString()));
            }
        }
        throw new RequiredFieldsException();
    }


}
