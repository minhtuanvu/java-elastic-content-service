package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceGetByIdTest {

    @Mock
    private ContentRepo contentRepo;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private ArticleService articleService;

    @Test
    public void shouldGetAndReturnPublishedContents() {
        //Given
        Long id = 1L;
        String body = "{\"state\" : \"published\", \"homeSection\" : { \"state\" : \"published\", \"hey\": \"ho\" }}";
        when(contentRepo.findById(id)).thenReturn(Mono.just(new Content(id, body)));

        //When
        Mono<String> result = articleService.get(id);

        //Then
        assertThat(result.block()).isEqualToIgnoringWhitespace(body);
    }

    @Test
    public void shouldGetAndSkipNonPublishedContents() {
        //Given
        Long id = 1L;
        String body = "{\"state\" : \"whatever\"}";
        when(contentRepo.findById(id)).thenReturn(Mono.just(new Content(id, body)));

        //When
        Mono<String> result = articleService.get(id);

        //Then
        assertThat(result.blockOptional()).isNotPresent();
    }
}