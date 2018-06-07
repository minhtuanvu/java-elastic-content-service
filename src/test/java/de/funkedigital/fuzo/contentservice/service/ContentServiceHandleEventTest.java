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

import java.io.IOException;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceHandleEventTest {

    @Spy
    ObjectMapper objectMapper;
    @Mock
    SaveContentFunction saveContentFunction;
    @InjectMocks
    private ContentService contentService;

    @Before
    public void setUp() throws Exception {
        Content content = new Content(1L, "");
        when(saveContentFunction.apply(any())).thenReturn(Mono.just(content));
    }

    @Test
    public void shouldHandleEvent() throws IOException {
        //When
        contentService.handleEvent(new Event(Event.ActionType.CREATE, 1L, objectMapper.createObjectNode())).block();

        //Then
        verify(saveContentFunction).apply(any());
    }

}