package com.starting.database.repositories;

import com.starting.database.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer> {
    List<AuthorEntity> findAuthorByAgeBefore(Integer ageBefore);
}
