package de.funkedigital.fuzo.contentservice.sitemap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By {kazi}
 */


public class SitemapController {

    @GetMapping(value = "sitemap", produces = MediaType.APPLICATION_XML_VALUE)
    public void fetchMostRecentSitemaps() {




    }
}
