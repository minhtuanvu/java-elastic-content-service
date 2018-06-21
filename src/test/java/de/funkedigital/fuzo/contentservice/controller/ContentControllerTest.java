package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.service.ArticleService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"elastic.host=localhost", "elastic.port=9200", "consumer.enabled=false"})
public class ContentControllerTest {

    @MockBean
    private ArticleService articleService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldRetrieveContentById() {
        //Given
        Long id = 1L;
        when(articleService.get(id)).thenReturn(Mono.just("{}"));

        //When //Then
        webTestClient.get().uri("/contents/{id}", id).accept(MediaType.APPLICATION_JSON)
                .exchange().expectBody(String.class).isEqualTo("{}");
    }

    @Test
    public void shouldReturnEmptyOnNonExistentContent() throws InterruptedException {
        //Given
        Long id = 1L;
        when(articleService.get(id)).thenReturn(Mono.create(MonoSink::success));

        //When //Then
        webTestClient.get().uri("/contents/{id}", id)
                .exchange().expectBody(String.class).isEqualTo("{}");
    }

    @Test
    public void shouldSearchByParams() {
        //Given
        when(articleService.searchBy(any())).thenReturn(Flux.just("{}", "{}"));

        //When //Then
        webTestClient.get().uri("/contents?homeSections=123&limit=9&&offset=2")
                .exchange().expectBody(String.class).isEqualTo("{}{}");

        ArgumentCaptor<ContentSearchRequest> argumentCaptor = ArgumentCaptor.forClass(ContentSearchRequest.class);
        verify(articleService).searchBy(argumentCaptor.capture());

        ContentSearchRequest contentSearchRequest = argumentCaptor.getValue();
        assertThat(contentSearchRequest.getHomeSections()).containsExactly("123");
        assertThat(contentSearchRequest.getLimit()).isEqualTo(9);
        assertThat(contentSearchRequest.getOffset()).isEqualTo(2);
    }
}