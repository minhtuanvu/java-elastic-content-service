package de.funkedigital.fuzo.contentservice.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    RestHighLevelClient restHighLevelClient(@Value("${elastic.host}") String host) {
        return new RestHighLevelClient(
                RestClient
                        .builder(new HttpHost(host))
                        .setRequestConfigCallback(config -> config
                                .setConnectTimeout(50_000)
                                .setConnectionRequestTimeout(50_000)
                                .setSocketTimeout(50_000)
                        )
                        .setMaxRetryTimeoutMillis(5_000));
    }
}
