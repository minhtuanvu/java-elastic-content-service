package de.funkedigital.fuzo.contentservice.repo;

import de.funkedigital.fuzo.contentservice.models.TopListResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TopListRepoFallback implements TopListRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopListRepoFallback.class);

    @Override
    public TopListResult getTopList() {
        LOGGER.warn("Returning empty toplist due to open circuit");
        return new TopListResult();
    }

}
