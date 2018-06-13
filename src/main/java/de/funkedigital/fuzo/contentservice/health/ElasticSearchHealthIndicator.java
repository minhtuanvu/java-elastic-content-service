package de.funkedigital.fuzo.contentservice.health;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchHealthIndicator implements HealthIndicator {

    private final RestHighLevelClient restHighLevelClient;

    ElasticSearchHealthIndicator(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Health health() {
        try {
            restHighLevelClient.get(new GetRequest("contents", "id", "1"));
        } catch (Exception e) {
            return Health.down(e).build();
        }
        return Health.up().build();
    }
}
