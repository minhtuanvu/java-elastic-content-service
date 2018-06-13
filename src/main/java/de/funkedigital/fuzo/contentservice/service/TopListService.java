package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.TopListEntry;
import de.funkedigital.fuzo.contentservice.models.TopListResult;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;
import de.funkedigital.fuzo.contentservice.repo.TopListRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import reactor.core.publisher.Flux;

@Service
public class TopListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopListService.class);

    private final TopListRepo topListRepo;
    private final ContentRepo contentRepo;

    TopListService(TopListRepo topListRepo,
                   ContentRepo contentRepo) {
        this.topListRepo = topListRepo;
        this.contentRepo = contentRepo;
    }


    public Flux<String> getTopListContents() {
        TopListResult topListResult = topListRepo
                .getTopList();
        LOGGER.info("Top list result: {}", topListResult);
        return contentRepo.findByIds(topListResult
                .getTopListEntries()
                .stream()
                .map(TopListEntry::getArticleId)
                .collect(Collectors.toSet()));
    }
}
