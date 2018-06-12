package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.service.EventService;

import org.reactivestreams.Publisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Just for testing
    @PostMapping("/events")
    public Publisher<Content> post(@RequestBody @Valid Event event) {
        return eventService.handleEvent(event);
    }

}
