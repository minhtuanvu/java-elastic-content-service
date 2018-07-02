package de.funkedigital.fuzo.sitemapservice.controller;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import de.funkedigital.fuzo.sitemapservice.service.SitemapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By {kazi}
 */

@RestController
public class SitemapController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapController.class);

    private SitemapService sitemapService;
    private WebSitemapGenerator sitemapGenerator;

    public SitemapController(SitemapService sitemapService, WebSitemapGenerator sitemapGenerator) {
        this.sitemapService = sitemapService;
        this.sitemapGenerator = sitemapGenerator;
    }

    @GetMapping(value = "/sitemap/news", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> fetchMostRecentNews() {

        WebSitemapGenerator sitemap = sitemapService.getNewsSitemap(sitemapGenerator);
        return ResponseEntity.ok().body(String.join("", sitemap.writeAsStrings()));

    }
}
