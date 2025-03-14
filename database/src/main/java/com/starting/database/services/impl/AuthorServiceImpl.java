package com.starting.database.services.impl;

import com.starting.database.domain.dto.AuthorDto;
import com.starting.database.domain.entities.AuthorEntity;
import com.starting.database.repositories.AuthorRepository;
import com.starting.database.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    public AuthorServiceImpl( AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    @Override
    public AuthorEntity save(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Override
    public List<AuthorEntity> findMany() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findAuthorById(int id) {
        return authorRepository.findById(id);
    }

    @Override
    public AuthorEntity partialUpdate(int id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.findById(id).map(existingAuthor->{
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            return authorRepository.save(existingAuthor);
        })
                .orElseThrow(()-> new RuntimeException("Author not found"));
    }

    @Override
    public void delete(int id) {
        authorRepository.deleteById(id);
    }


}
