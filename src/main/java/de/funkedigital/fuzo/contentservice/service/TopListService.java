package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.repo.ContentRepo;
import de.funkedigital.fuzo.contentservice.repo.TopListRepo;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class TopListService {

    private final TopListRepo topListRepo;
    private final ContentRepo contentRepo;

    TopListService(TopListRepo topListRepo,
                   ContentRepo contentRepo) {
        this.topListRepo = topListRepo;
        this.contentRepo = contentRepo;
    }


    public Flux<String> getTopListContents(String publication) {
        return contentRepo.findByIds(topListRepo.getTopList(publication));
    }
}
