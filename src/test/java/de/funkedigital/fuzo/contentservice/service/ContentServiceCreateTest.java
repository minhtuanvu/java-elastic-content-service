package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.exceptions.RequiredFieldsException;
import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceCreateTest {

    @Mock
    private ContentRepo contentRepo;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private ContentService contentService;

    @Test
    public void shouldMapIdAndBody() throws IOException {
        //Given
        Long id = 1L;
        String body = "{\"articleId\" : \"1\"}";
        Content content = new Content(id, body);
        when(contentRepo.save(any())).thenReturn(Mono.just(content));
        ArgumentCaptor<Content> contentArgumentCaptor = ArgumentCaptor.forClass(Content.class);

        //When
        Content resultContent = contentService.create(body).block();

        //Then
        assertThat(resultContent.getBody()).isEqualTo(body);
        assertThat(resultContent.getId()).isEqualTo(id);
        verify(contentRepo).save(contentArgumentCaptor.capture());
        assertThat(contentArgumentCaptor.getValue()).isEqualTo(content);
    }

    @Test
    public void shouldThrowExceptionOnEmptyId() {
        //Given
        String body = "{\"articleId\" : \"\"}";

        //When
        assertThatThrownBy(() -> contentService.create(body))
                .isInstanceOf(RequiredFieldsException.class);
    }

    @Test
    public void shouldThrowExceptionOnAbsentId() {
        //Given
        String body = "{}";

        //When
        assertThatThrownBy(() -> contentService.create(body))
                .isInstanceOf(RequiredFieldsException.class);
    }
}