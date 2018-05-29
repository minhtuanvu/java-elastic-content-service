package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

    private static final ResponseEntity<Content> NOT_FOUND = ResponseEntity.notFound().build();
    private final ContentRepo contentRepo;

    ContentController(ContentRepo contentRepo) {
        this.contentRepo = contentRepo;
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return contentRepo.findById(id).map(ResponseEntity::ok).orElse(NOT_FOUND);
    }

}
