package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.Event;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SaveContentFunctionTest {

    @Mock
    private ContentRepo contentRepo;
    @Spy
    private ObjectMapper objectMapper;
    @InjectMocks
    private SaveContentFunction saveContentFunction;

    @Before
    public void setup() {
        //Given
        when(contentRepo.save(any())).thenReturn(Mono.just(new Content(1L, "{\"payload\" : {}}")));

        //When
        saveContentFunction.apply(new Event(Event.ActionType.CREATE, 1L, objectMapper.createObjectNode()));
    }

    @Test
    public void shouldSaveContent() {
        ArgumentCaptor<Content> contentArgumentCaptor = ArgumentCaptor.forClass(Content.class);
        verify(contentRepo).save(contentArgumentCaptor.capture());
        Assertions.assertThat(contentArgumentCaptor.getValue()).isEqualTo(new Content(1L, "{}"));
    }
}