package com.starting.database.controllers;

import com.starting.database.domain.dto.BookDto;
import com.starting.database.domain.entities.BookEntity;
import com.starting.database.mappers.Mapper;
import com.starting.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity,BookDto> bookMapper;
    private BookService bookService;
    public BookController(Mapper<BookEntity,BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }
    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        boolean ifExists = bookService.findBookById(isbn).isPresent();
        BookEntity savedBook = bookService.createUpdateBook(isbn,bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(savedBook);
        if(!ifExists) {
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        }

    }
    @GetMapping("/books")
    public Page<BookDto> getAllBooks(Pageable pagable) {
        Page<BookEntity> books = bookService.findAll(pagable);
        return books.map( bookMapper::mapTo);
    }
    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> bookEntity = bookService.findBookById(isbn);
        return bookEntity.map( bookEntity1 -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity1);
            return new ResponseEntity<>(bookDto,HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        if(bookService.findBookById(isbn).isPresent()) {
            BookEntity bookEntity = bookMapper.mapFrom(bookDto);
            BookEntity updatedBook = bookService.partialUpdate(isbn,bookEntity);
            BookDto savedBookDto = bookMapper.mapTo(updatedBook);
            return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
        bookService.deleteBook(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
