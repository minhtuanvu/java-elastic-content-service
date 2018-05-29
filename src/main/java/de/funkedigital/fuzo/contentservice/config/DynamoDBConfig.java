package de.funkedigital.fuzo.contentservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "de.funkedigital.fuzo.contentservice.repo")
public class DynamoDBConfig {
    @Bean
    public AmazonDynamoDB amazonDynamoDB(@Value("${amazon.dynamodb.endpoint}") String endpoint,
                                         @Value("${cloud.aws.region.static}") String region,
                                         @Value("${amazon.aws.accesskey}") String accessKey,
                                         @Value("${amazon.aws.secretkey}") String secretKey) {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region)).build();
    }
}
