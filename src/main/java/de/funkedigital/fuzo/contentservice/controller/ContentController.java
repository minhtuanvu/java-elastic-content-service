package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.service.ArticleService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ContentController {

    private final ArticleService articleService;

    public ContentController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/contents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> get(@PathVariable Long id) {
        return articleService.get(id);
    }

    @GetMapping("/contents")
    public Flux<String> search(@ModelAttribute ContentSearchRequest contentSearchRequest) {
        return articleService.searchBy(contentSearchRequest);
    }
}
