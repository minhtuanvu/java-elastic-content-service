package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.exceptions.RequiredFieldsException;
import de.funkedigital.fuzo.contentservice.models.Content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
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
        when(saveContentFunction.apply(any(), anyLong())).thenReturn(Mono.just(content));
    }

    @Test
    public void shouldHandleCreateEvent() throws IOException {
        //When
        contentService.handleEvent("{\"objectId\": 1, \"actionType\": \"create\"}");

        //Then
        verify(saveContentFunction).apply(any(), anyLong());
    }

    @Test
    public void shouldHandleUpdateEvent() throws IOException {
        //When
        contentService.handleEvent("{\"objectId\": 1, \"actionType\": \"update\"}");

        //Then
        verify(saveContentFunction).apply(any(), anyLong());
    }

    @Test
    public void shouldThrowExceptionWhenNoIdField() {
        assertThatThrownBy(() -> {
            contentService.handleEvent("{ \"actionType\": \"update\"}");
        }).isInstanceOf(RequiredFieldsException.class);
    }

    @Test
    public void shouldThrowExceptionWhenNoActionTypeField() {
        assertThatThrownBy(() -> {
            contentService.handleEvent("{\"objectId\": 1}");
        }).isInstanceOf(RequiredFieldsException.class);
    }
}