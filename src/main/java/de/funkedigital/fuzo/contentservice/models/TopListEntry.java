package de.funkedigital.fuzo.contentservice.models;

public class TopListEntry {

    private Long articleId;

    private Integer pageViews;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    @Override
    public String toString() {
        return "TopListEntry{" +
                "articleId=" + articleId +
                ", pageViews=" + pageViews +
                '}';
    }
}
