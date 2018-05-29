package de.funkedigital.fuzo.contentservice.repo;

import de.funkedigital.fuzo.contentservice.models.Content;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface ContentRepo extends CrudRepository<Content, Long> {
}