package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.service.TopListService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@ConditionalOnProperty("toplist.enabled")
public class TopListController {

    private final TopListService topListService;

    TopListController(TopListService topListService) {
        this.topListService = topListService;
    }

    @Cacheable("toplists")
    @GetMapping("/toplists/")
    public Flux<String> topList() {
        return topListService.getTopListContents();
    }
}
