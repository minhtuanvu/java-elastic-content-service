package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceHandleEventTest {

    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private SaveContentFunction saveContentFunction;
    @InjectMocks
    private ContentService contentService;

    @Before
    public void setUp() {
        Content content = new Content(1L, "");
        when(saveContentFunction.apply(any())).thenReturn(Mono.just(content));
    }

    @Test
    public void shouldHandleEvent() {
        //When
        contentService.handleEvent(new Event(Event.ActionType.CREATE, 1L, objectMapper.createObjectNode())).block();

        //Then
        verify(saveContentFunction).apply(any());
    }

}