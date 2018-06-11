package de.funkedigital.fuzo.contentservice.repo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@FeignClient(url = "${ga.url}", name = "ga", fallback = TopListRepoFallback.class)
public interface TopListRepo {

    @RequestMapping(method = RequestMethod.GET, path = "/toplists/{publication}")
    Set<Long> getTopList(@PathVariable("publication") String publication);

}
