package de.funkedigital.fuzo.sitemapservice.repo;

import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.StateFields;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class SitemapRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapRepo.class);
    private final RestHighLevelClient restHighLevelClient;

    private static final String CONTENT_INDEX = "contents";
    private final String[] includes = new String[]{};
    private final String[] excludes = new String[]{"fields.body"};

    public SitemapRepo(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }


    public Flux<String> search(ContentSearchRequest contentSearchRequest, StateFields extra) {
        return Flux.fromStream(() -> streamSearch(contentSearchRequest, extra));
    }

    private Stream<String> streamSearch(ContentSearchRequest contentSearchRequest, StateFields extra) {
        try {
            return StreamSupport.stream(restHighLevelClient.search(new SearchRequest(CONTENT_INDEX)
                    .source(new SearchSourceBuilder()
                            .query(
                                    buildQuery(contentSearchRequest, extra)
                            )
                            .from(contentSearchRequest.getOffset())
                            .size(contentSearchRequest.getLimit())
                            .sort(new FieldSortBuilder("lastModified").order(SortOrder.DESC))
                            .fetchSource(includes, excludes)))
                    .getHits().spliterator(), false)
                    .map(SearchHit::getSourceAsString)
                    .filter(Objects::nonNull);
        } catch (IOException e) {
            LOGGER.error("Failed to search", e);
            return Stream.empty();
        }
    }

    private QueryBuilder buildQuery(ContentSearchRequest contentSearchRequest, StateFields stateFields) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("state", stateFields.getState()))
                .must(QueryBuilders.termsQuery("homeSection.state", stateFields.getHomeSection().getState()))
                .must(QueryBuilders.termsQuery("contentType", contentSearchRequest.getContentTypeSet()))
                ;
    }
}
