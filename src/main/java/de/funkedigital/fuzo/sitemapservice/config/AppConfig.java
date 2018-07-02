package de.funkedigital.fuzo.sitemapservice.config;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;

@Configuration
public class AppConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    WebSitemapGenerator sitemapGenerator(@Value("${sitemap.base-url}") String baseUrl) throws MalformedURLException {
        return new WebSitemapGenerator(baseUrl);
    }
}
