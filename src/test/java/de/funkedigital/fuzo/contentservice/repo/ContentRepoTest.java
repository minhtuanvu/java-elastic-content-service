package de.funkedigital.fuzo.contentservice.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.Section;
import de.funkedigital.fuzo.contentservice.models.StateFields;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"elastic.host=localhost", "elastic.port=9200", "consumer.enabled=false"})
public class ContentRepoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepoTest.class);

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .file("src/test/resources/docker-compose.yml")
            .waitingForService("elasticsearch", HealthChecks
                    .toRespond2xxOverHttp(9200, dockerPort -> dockerPort.inFormat("http://$HOST:$EXTERNAL_PORT")))
            .build();

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ContentRepo contentRepo;

    private final String body = "{\"key\" : \"value\", \"homeSection\" : { \"sectionId\" : \"123\", \"state\": \"published\" }, \"state\": \"published\"}";

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        assertThat(restHighLevelClient.info().getClusterName().value()).isEqualTo("docker-cluster");

        try {
            restHighLevelClient.indices().delete(new DeleteIndexRequest("contents"));
        } catch (Exception e) {
            LOGGER.warn("ignoring index delete error");
        }

        restHighLevelClient.index(new IndexRequest("contents", "id", "1")
                .source(body, XContentType.JSON));

        assertThat(restHighLevelClient.exists(new GetRequest("contents", "id", "1")))
                .isTrue();
    }

    @Test
    public void shouldSaveInContentIndexWithIdField() throws IOException {
        //Given //When
        Content result = contentRepo.save(new Content(2L, "{\"key\" : \"value\"}"));

        //Then
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(restHighLevelClient.exists(new GetRequest("contents", "id", "2")))
                .isTrue();
    }

    @Test
    public void shouldFindExistentContentByID() {
        //When
        Mono<Content> result = contentRepo.findById(1L);

        //Then
        assertThat(requireNonNull(result.block()).getId()).isEqualTo(1L);
        assertThat(requireNonNull(result.block()).getBody()).isEqualToIgnoringWhitespace(body);
    }

    @Test
    public void shouldReturnEmptyOnNonExistentContent() {
        //When
        Mono<Content> result = contentRepo.findById(2L);

        //Then
        assertThat(result.blockOptional()).isNotPresent();
    }

    @Test
    @Ignore
    public void shouldSearchExistentContentByQuery() throws IOException, InterruptedException {
        //Given
        ContentSearchRequest contentSearchRequest = new ContentSearchRequest();

        //When
        Flux<String> result = contentRepo.search(contentSearchRequest, new StateFields("published", new StateFields("published", null)));

        //Then
        Thread.sleep(2000L);
        assertThat(result.toStream()
                .map(s -> {
                    try {
                        return objectMapper.readTree(s);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()))
                .containsExactly(objectMapper.readTree(body));
    }

    @Test
    public void shouldDeleteSuccessfully() throws IOException {
        contentRepo.delete(1L);
        assertThat(restHighLevelClient.exists(new GetRequest("contents", "id", "2")))
                .isFalse();
    }

    @Test
    public void shouldUpdateSectionSuccessfully() throws IOException, InterruptedException {
        Section section = new Section(123L, "updated name", "updated unique name", "updated dir", "updated path", "published", Collections.emptyList());
        Thread.sleep(2000L);
        List<Content> result = contentRepo.updateSection(
                section);
        assertThat(result).extracting(Content::getId).containsExactly(1L);

        JsonNode contentBody = objectMapper.readTree(requireNonNull(contentRepo.findById(1L).block()).getBody());

        assertThat(objectMapper.treeToValue(contentBody.get("homeSection"), Section.class)).isEqualTo(section);
    }

}