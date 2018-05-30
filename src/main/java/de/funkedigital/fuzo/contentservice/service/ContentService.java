package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.exceptions.RequiredFieldsException;
import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Service
public class ContentService {

    private static final String ARTICLE_ID_FIELD = "articleId";
    private final ContentRepo contentRepo;
    private final ObjectMapper objectMapper;

    ContentService(ContentRepo contentRepo,
                   ObjectMapper objectMapper) {
        this.contentRepo = contentRepo;
        this.objectMapper = objectMapper;
    }

    public Optional<String> get(Long id) {
        return contentRepo.findById(id).map(Content::getBody);
    }

    public Content create(String contentString) throws IOException {
        JsonNode node = objectMapper.readTree(contentString);
        JsonNode articleIdNode = node.get(ARTICLE_ID_FIELD);
        if (articleIdNode != null && !StringUtils.isEmpty(articleIdNode.asText())) {
            return contentRepo.save(new Content(articleIdNode.asLong(), contentString));
        }
        throw new RequiredFieldsException();
    }
}
