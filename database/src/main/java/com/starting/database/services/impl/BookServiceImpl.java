package com.starting.database.services.impl;

import com.starting.database.domain.entities.BookEntity;
import com.starting.database.repositories.BookRepository;
import com.starting.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findMany() {
        Iterable<BookEntity> bookEntities = bookRepository.findAll();
        return StreamSupport.stream(bookEntities.spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findBookById(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.findById(isbn).map(existingBook->{
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepository.save(existingBook);
        }).orElseThrow(()-> new RuntimeException("Book doese not exist"));
    }

    @Override
    public void deleteBook(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
