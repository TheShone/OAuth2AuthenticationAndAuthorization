
package com.starting.database.repositories;

import com.starting.database.TestDataUtil;
import com.starting.database.domain.entities.AuthorEntity;
import com.starting.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {
    private final BookRepository underTest;
    private final AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest, AuthorRepository authorRepository) {
        this.underTest = underTest;
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatBookCanBeCreatedAndRetrieved() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        authorRepository.save(author);
        BookEntity bookEntity = TestDataUtil.createTestBookA(author);
        bookRepository.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }


    @Test
    public void testThatBooksCanBeRetrieved() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        authorRepository.save(author);
        BookEntity bookEntity = TestDataUtil.createTestBookA(author);
        underTest.save(bookEntity);

        AuthorEntity author2 = TestDataUtil.createTestAuthorB();
        author2.setId(null);
        authorRepository.save(author2);
        BookEntity bookEntity2 = TestDataUtil.createTestBookB(author2);
        underTest.save(bookEntity2);

        AuthorEntity author3 = TestDataUtil.createTestAuthorC();
        author3.setId(null);
        authorRepository.save(author3);
        BookEntity bookEntity3 = TestDataUtil.createTestBookC(author3);
        underTest.save(bookEntity3);

        Iterable<BookEntity> results = underTest.findAll();
        assertThat(results)
                .isNotEmpty()
                .contains(bookEntity, bookEntity2, bookEntity3);

    }

   @Test
    public void testThatBooksCanBeUpdated() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        authorRepository.save(author);
        BookEntity bookEntity = TestDataUtil.createTestBookA(author);
        underTest.save(bookEntity);
        bookEntity.setTitle("Gospodja Ministarka");
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }
    
    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        authorRepository.save(author);
        BookEntity bookEntity = TestDataUtil.createTestBookA(author);
        underTest.save(bookEntity);
        underTest.deleteById(bookEntity.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isNotPresent();
    }
}

