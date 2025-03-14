package com.starting.database.services;

import com.starting.database.domain.entities.AuthorEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AuthorService {

    public AuthorEntity save(AuthorEntity author);
    public List<AuthorEntity> findMany();
    public Optional<AuthorEntity> findAuthorById(int id);

    AuthorEntity partialUpdate(int id, AuthorEntity authorEntity);
    public void delete(int id);
}
