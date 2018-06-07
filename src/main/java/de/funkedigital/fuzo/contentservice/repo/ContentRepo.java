package de.funkedigital.fuzo.contentservice.repo;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class ContentRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepo.class);

    private final RestHighLevelClient restHighLevelClient;
    private final String[] includes = new String[]{};
    private final String[] excludes = new String[]{"fields.body"};

    ContentRepo(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public Mono<Content> save(Content content) {
        return Mono.create(sink -> {
            IndexRequest indexRequest = new IndexRequest("contents", "id", String.valueOf(content.getId()));
            indexRequest.source(content.getBody(), XContentType.JSON);
            restHighLevelClient.indexAsync(indexRequest, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    sink.success(content);
                }

                @Override
                public void onFailure(Exception e) {
                    sink.error(e);
                }
            });
        });
    }

    public Mono<Content> findById(Long id) {
        return Mono.create(sink -> restHighLevelClient
                .getAsync(new GetRequest("contents", "id", String.valueOf(id)),
                        new ActionListener<GetResponse>() {
                            @Override
                            public void onResponse(GetResponse getFields) {
                                sink.success(new Content(id, getFields.getSourceAsString()));
                            }

                            @Override
                            public void onFailure(Exception e) {
                                sink.error(e);
                            }
                        }));
    }

    public Flux<String> search(ContentSearchRequest contentSearchRequest) {
        return Flux.fromStream(() -> streamSearch(contentSearchRequest));
    }

    private Stream<String> streamSearch(ContentSearchRequest contentSearchRequest) {
        try {
            return StreamSupport.stream(restHighLevelClient.search(new SearchRequest("contents")
                    .source(new SearchSourceBuilder()
                            .query(QueryBuilders.termsQuery("homeSection.uniqueName", contentSearchRequest.getHomeSections()))
                            .from(contentSearchRequest.getOffset())
                            .size(contentSearchRequest.getLimit())
                            .fetchSource(includes, excludes)))
                    .getHits().spliterator(), false).map(SearchHit::getSourceAsString);
        } catch (IOException e) {
            LOGGER.error("Failed to search", e);
            return Stream.empty();
        }
    }

}
