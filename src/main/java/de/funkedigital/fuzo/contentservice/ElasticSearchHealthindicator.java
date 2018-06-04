package de.funkedigital.fuzo.contentservice;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchHealthindicator implements HealthIndicator {


    private final RestHighLevelClient restHighLevelClient;

    ElasticSearchHealthindicator(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Health health() {
        try {
            restHighLevelClient.get(new GetRequest("contents", "id", "1"));
        } catch (IOException e) {
            return Health.down(e).build();
        }
        return Health.up().build();
    }
}
