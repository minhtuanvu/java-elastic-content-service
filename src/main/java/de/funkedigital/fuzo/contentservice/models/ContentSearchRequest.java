package de.funkedigital.fuzo.contentservice.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ContentSearchRequest {


    private String[] homeSections;

    private int limit = 100;

    private int offset = 0;

    private String publicationShortName;

    private Set<String> contentTypeSet=new HashSet<>();

    private LocalDateTime lastModifiedDate = LocalDateTime.now().minusHours(1);


    public ContentSearchRequest() {

    }

    public ContentSearchRequest(int limit, String publicationShortName) {
        this.limit = limit;
        this.publicationShortName = publicationShortName;

    }

    public String getPublicationShortName() {
        return publicationShortName;
    }

    public void setPublicationShortName(String publicationShortName) {
        this.publicationShortName = publicationShortName;
    }

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

    public Set<String> getContentTypeSet() {
        return contentTypeSet;
    }

    public void setContentTypeSet(Set<String> contentTypeSet) {
        this.contentTypeSet = contentTypeSet;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
