package de.funkedigital.fuzo.contentservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TopListResult {

    private String key;

    @JsonProperty("analyticsList")
    private List<TopListEntry> topListEntries;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<TopListEntry> getTopListEntries() {
        return topListEntries;
    }

    public void setTopListEntries(List<TopListEntry> topListEntries) {
        this.topListEntries = topListEntries;
    }

    @Override
    public String toString() {
        return "TopListResult{" +
                "key='" + key + '\'' +
                ", topListEntries=" + topListEntries +
                '}';
    }
}
