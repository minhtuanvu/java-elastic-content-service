
package de.funkedigital.fuzo.sitemapservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import de.funkedigital.fuzo.contentservice.models.ContentSearchRequest;
import de.funkedigital.fuzo.contentservice.models.StateFields;
import de.funkedigital.fuzo.contentservice.service.UrlService;
import de.funkedigital.fuzo.sitemapservice.repo.SitemapRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SitemapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapService.class);

    private SitemapRepo sitemapRepo;
    private UrlService urlService;
    private final ObjectMapper objectMapper;
    private String publicationName;


    public SitemapService(SitemapRepo sitemapRepo, UrlService urlService, ObjectMapper objectMapper, @Value("${publication.name:fuzo}") String publicationName) {
        this.sitemapRepo = sitemapRepo;
        this.urlService = urlService;
        this.objectMapper = objectMapper;
        this.publicationName = publicationName;
    }

    public WebSitemapGenerator getNewsSitemap(WebSitemapGenerator sitemap) {

        fetchLatestThousandNews().toStream().forEach(sitemapItem -> {
            try {
                JsonNode jsonSitemapItem = objectMapper.readTree(sitemapItem);
                sitemap.addUrl(urlService.buildDefaultUrl(jsonSitemapItem));
            } catch (IOException ioe) {
                LOGGER.warn("error parsing sitemap-item: {}. Will continue...", ioe.getMessage());
            }
        });

        return sitemap;

    }

    private Flux<String> fetchLatestThousandNews() {

        ContentSearchRequest contentSearchRequest = new ContentSearchRequest(1000, publicationName);
        final Set<String> contentTypeSet = Stream.of("news", "opinion", "printimport", "recipe").collect(Collectors.toSet());
        contentSearchRequest.setContentTypeSet(contentTypeSet);

        return sitemapRepo.search(contentSearchRequest, new StateFields("published", new StateFields("published")));
    }







}