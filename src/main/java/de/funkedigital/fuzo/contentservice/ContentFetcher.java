package de.funkedigital.fuzo.contentservice;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ContentFetcher {

    static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        int start = 0;
        while (processStartingAt(start)) {
            start += 1000;
        }

    }

    private static boolean processStartingAt(int i) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://uat-be:8983/solr/default_core/select?fl=objectid,publishdate,contenttype,state&fq=NOT%20contenttype:%22widget_ad%22&fq=NOT%20contenttype:%22widget_contentteaser%22&fq=NOT%20contenttype:%22widget_contentteaser24%22&fq=NOT%20contenttype:%22widget_xhtml%22&fq=publication:fuzo&fq=state:published&indent=on&q=*:*&rows=1000&sort=publishdate%20DESC&wt=json&start=" + i);
        UriComponents components = builder.build(true);
        URI uri = components.toUri();
        WebClient webClient = WebClient.create("http://localhost:8080/");
        JsonNode result = restTemplate.getForObject(uri, JsonNode.class);
        JsonNode docs = result.get("response").get("docs");
        Stream<CompletableFuture<Void>> futures = StreamSupport.stream(docs.spliterator(), false).map(jsonNode -> CompletableFuture.runAsync(() -> {
            String jsonContent = restTemplate.getForObject("http://uat-be:8080/fuzo/template/framework/tools/article.v2.json.jsp?articleId={id}", String.class, jsonNode.get("objectid").asText());
            webClient.post().uri("/content").body(BodyInserters.fromObject(jsonContent)).exchange().block();
        }, executorService));

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        return docs != null;

    }

}
