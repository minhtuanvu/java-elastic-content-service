package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.exceptions.RequiredFieldsException;
import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

import reactor.core.publisher.Mono;

@Service
public class ContentService {

    private static final String ARTICLE_ID_FIELD = "objectId";
    private static final String ACTION_TYPE_FIELD = "actionType";
    private static final String PAYLOAD_FIELD = "payload";
    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;

    public enum ActionType {
        CREATE, UPDATE
    }

    ContentService(ContentRepo contentRepo,
                   ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
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
                switch (actionType) {
                    case UPDATE:
                    case CREATE:
                        return contentRepo.save(new Content(articleIdNode.asLong(),
                                objectMapper.writeValueAsString(node.get(PAYLOAD_FIELD))));
                    default:
                        throw new UnsupportedOperationException(actionType.toString());
                }
            }
        }
        throw new RequiredFieldsException();
    }
}
