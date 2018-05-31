package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.service.ContentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest
public class ContentControllerTest {

    @MockBean
    private ContentService contentService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldReturnBodyOnExistentContent() {
        //Given
        Long id = 1L;
        String body = "{\"key\" : \"value\"}";
        when(contentService.get(id)).thenReturn(Mono.just(body));

        //When //Then
        webTestClient.get().uri("/content/{id}", id).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(body);
    }

    @Test
    public void shouldReturnEmptyOnNonExistentContent() {
        //Given
        Long id = 1L;
        when(contentService.get(id)).thenReturn(Mono.empty());

        //When //Then
        //When //Then
        webTestClient.get().uri("/content/{id}", id).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(null);
    }
}