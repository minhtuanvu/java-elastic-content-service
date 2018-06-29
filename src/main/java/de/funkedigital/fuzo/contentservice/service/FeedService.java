package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.atom.Category;
import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.atom.Person;
import com.rometools.rome.feed.synd.SyndPerson;

import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class FeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedService.class);

    private static final String FEED_TYPE = "atom_1.0";
    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    FeedService(ArticleService articleService,
                ObjectMapper objectMapper) {
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    public Feed getFeed() {

        Feed feed = new Feed();
        feed.setFeedType(FEED_TYPE);
        feed.setTitle("fuzo");
        feed.setId("https://fuzo/");

        Content subtitle = new Content();
        subtitle.setType("text/plain");
        subtitle.setValue("fuzo");
        feed.setSubtitle(subtitle);

        Date postDate = new Date();
        feed.setUpdated(postDate);

        feed.setEntries(articleService.searchBy(new ContentSearchRequest())
                .map(this::toFeedEntry)
                .filter(Objects::nonNull)
                .collectList().block());

        return feed;

    }

    private Entry toFeedEntry(String contentString) {
        try {
            JsonNode contentNode = objectMapper.readTree(contentString);
            JsonNode fieldsNode = contentNode.get("fields");
            JsonNode homeSectionNode = contentNode.get("homeSection");

            Date postDate = new Date();
            Entry entry = new Entry();

            Link link = new Link();
            link.setHref(contentNode.get("previewUrl").asText());
            entry.setAlternateLinks(Collections.singletonList(link));
            SyndPerson author = new Person();

            author.setName(Optional.ofNullable(contentNode.get("author_short"))
                    .map(JsonNode::asText)
                    .orElse(null));

            entry.setAuthors(Collections.singletonList(author));
            entry.setCreated(postDate);
            entry.setPublished(postDate);
            entry.setUpdated(postDate);
            entry.setId(link.getHref());
            entry.setTitle(contentNode.get("title").asText());

            Category category = new Category();
            category.setTerm(homeSectionNode.get("name").asText());
            entry.setCategories(Collections.singletonList(category));

            Content summary = new Content();
            summary.setType("text/plain");

            summary.setValue(Optional.ofNullable(fieldsNode.get("seo_description"))
                    .map(JsonNode::asText)
                    .orElse(null));

            entry.setSummary(summary);

            return entry;
        } catch (Exception e) {
            LOGGER.warn("Unable to map content string into feed", e);
            return null;
        }
    }

}
