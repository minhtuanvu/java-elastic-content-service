package de.funkedigital.fuzo.contentservice.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class TopListRepoFallback implements TopListRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopListRepoFallback.class);

    @Override
    public Set<Long> getTopList(String publication) {
        LOGGER.warn("Returning empty toplist due to open circuit");
        return Collections.emptySet();
    }

}
