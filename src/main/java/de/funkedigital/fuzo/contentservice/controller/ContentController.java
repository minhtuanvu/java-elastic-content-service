package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.service.ContentService;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class ContentController {

    private final ContentService contentService;

    ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "/content/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> get(@PathVariable Long id) {
        return contentService.get(id);
    }

    @GetMapping("/content")
    public Flux<String> search(@ModelAttribute ContentSearchRequest contentSearchRequest) {
        return contentService.searchBy(contentSearchRequest);
    }

    //Just for testing
    @PostMapping("/content")
    public Mono<Content> post(@RequestBody @Valid Event event) {
        return contentService.handleEvent(event);
    }

}
