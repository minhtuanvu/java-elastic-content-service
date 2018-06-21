package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.service.ArticleService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Cacheable("contents")
    @GetMapping(value = "/contents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> get(@PathVariable Long id) {

        return articleService.get(id).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/contents")
    public Flux<String> search(@ModelAttribute ContentSearchRequest contentSearchRequest) {
        return articleService.searchBy(contentSearchRequest);
    }
}
