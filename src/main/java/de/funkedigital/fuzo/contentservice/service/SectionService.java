package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.models.Section;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SectionService.class);
    private final ObjectMapper objectMapper;
    private final ContentRepo contentRepo;

    SectionService(ObjectMapper objectMapper,
                   ContentRepo contentRepo) {
        this.objectMapper = objectMapper;
        this.contentRepo = contentRepo;
    }

    List<Content> handleEvent(Event event) {
        LOGGER.info("Handling section event: {}", event);
        if (event.getActionType() == Event.ActionType.UPDATE) {
            try {
                Section section = objectMapper.treeToValue(event.getPayload(), Section.class);
                return updateSection(section);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.warn("Section {} event was ignored", event.getActionType());
        return Collections.emptyList();
    }

    private List<Content> updateSection(Section section) {
        List<Content> result = contentRepo.updateSection(section);
        section.getSubSections()
                .stream()
                .map(this::updateSection)
                .forEach(result::addAll);
        return result;
    }

}
