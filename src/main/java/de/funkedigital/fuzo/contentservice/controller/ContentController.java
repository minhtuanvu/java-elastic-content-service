package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.service.ContentService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ContentController {

    private static final ResponseEntity<String> NOT_FOUND = ResponseEntity.notFound().build();
    private final ContentService contentService;

    ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "/content/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return contentService.get(id).map(ResponseEntity::ok).orElse(NOT_FOUND);
    }

    //Just for testing
    @PostMapping("/content")
    public Content post(@RequestBody String contentString) throws IOException {
        return contentService.create(contentString);
    }

}
