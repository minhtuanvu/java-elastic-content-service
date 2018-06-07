package de.funkedigital.fuzo.contentservice.models;

public class ContentSearchRequest {

    private String[] homeSections;

    private int limit = 100;

    private int offset = 0;

    public String[] getHomeSections() {
        return homeSections;
    }

    public void setHomeSections(String[] homeSections) {
        this.homeSections = homeSections;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
