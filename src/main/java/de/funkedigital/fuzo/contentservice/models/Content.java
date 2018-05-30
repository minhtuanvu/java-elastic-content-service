package de.funkedigital.fuzo.contentservice.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

@DynamoDBTable(tableName = "fuzo-content")
public class Content {

    public Content(Long id, String body) {
        this.id = id;
        this.body = body;
    }

    public Content() {
    }

    private Long id;

    @JsonIgnore
    private String body;

    @DynamoDBHashKey(attributeName = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content content = (Content) o;
        return Objects.equals(id, content.id) &&
                Objects.equals(body, content.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, body);
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", body='" + body + '\'' +
                '}';
    }
}
