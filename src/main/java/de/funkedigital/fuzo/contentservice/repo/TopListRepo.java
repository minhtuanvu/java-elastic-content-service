package de.funkedigital.fuzo.contentservice.repo;

import de.funkedigital.fuzo.contentservice.models.TopListResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${ga.url}", name = "ga", fallback = TopListRepoFallback.class)
public interface TopListRepo {

    @RequestMapping(method = RequestMethod.GET)
    TopListResult getTopList();

}
