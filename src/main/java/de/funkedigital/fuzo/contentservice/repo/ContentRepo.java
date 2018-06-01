package de.funkedigital.fuzo.contentservice.repo;

import de.funkedigital.fuzo.contentservice.models.Content;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;


@Repository
public class ContentRepo {

    private final RestHighLevelClient restHighLevelClient;

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
}
