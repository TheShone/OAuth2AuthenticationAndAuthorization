package com.starting.database.services;

import com.starting.database.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity createUpdateBook(String isbn, BookEntity bookEntity);

    List<BookEntity> findMany();

    Page<BookEntity> findAll(Pageable pageable);

    Optional<BookEntity> findBookById(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);
    void deleteBook(String isbn);
}
