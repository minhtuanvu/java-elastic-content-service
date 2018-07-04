package de.funkedigital.fuzo.contentservice.controller;

import com.rometools.rome.feed.atom.Feed;

import de.funkedigital.fuzo.contentservice.service.FeedService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    private final FeedService feedService;

    FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @Cacheable
    @GetMapping(value = "/feeds", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public Feed feed() {
        return feedService.getFeed();
    }

}
