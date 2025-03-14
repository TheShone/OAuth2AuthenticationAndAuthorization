package com.starting.database.controllers;

import com.starting.database.domain.dto.AuthorDto;
import com.starting.database.domain.entities.AuthorEntity;
import com.starting.database.mappers.Mapper;
import com.starting.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path="/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity=authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path="/authors")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorEntity> authors = authorService.findMany();
        List<AuthorDto> result = authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") int id) {
        Optional<AuthorEntity> author = authorService.findAuthorById(id);
        return author.map(authorEntity ->
                {
                    AuthorDto authorDto = authorMapper.mapTo(authorEntity);
                    return new ResponseEntity<>(authorDto, HttpStatus.OK);
                }
        ).orElse( new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable("id") int id, @RequestBody AuthorDto author) {
        if(authorService.findAuthorById(id).isPresent()) {
            author.setId(id);
            AuthorEntity authorEntity=authorMapper.mapFrom(author);
            return new ResponseEntity<>(authorMapper.mapTo(authorService.save(authorEntity)), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @PatchMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> PartialUpdate(@PathVariable("id") int id, @RequestBody AuthorDto author)
    {
        if(authorService.findAuthorById(id).isPresent()) {
            AuthorEntity authorEntity=authorMapper.mapFrom(author);
            AuthorEntity updatedAuthor = authorService.partialUpdate(id,authorEntity);
            AuthorDto savedAuthorDto = authorMapper.mapTo(updatedAuthor);
            return new ResponseEntity<>(savedAuthorDto, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(path="/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") int id) {
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
