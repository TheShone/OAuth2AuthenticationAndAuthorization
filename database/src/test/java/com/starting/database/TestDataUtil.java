package com.starting.database;

import com.starting.database.domain.dto.AuthorDto;
import com.starting.database.domain.dto.BookDto;
import com.starting.database.domain.entities.AuthorEntity;
import com.starting.database.domain.entities.BookEntity;

public final class TestDataUtil {
    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1)
                .name("Abigail Rose")
                .age(80)
                .build();
    }
    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1)
                .name("Abigail Rose")
                .age(80)
                .build();
    }
    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2)
                .name("Dobrica Cosic")
                .age(77)
                .build();
    }
    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3)
                .name("Dobrica Eric")
                .age(83)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("asdk123hksjdh")
                .title("Orlovi rano lete")
                .author(author)
                .build();
    }
    public static BookDto createTestBookDtoA(final AuthorDto author) {
        return BookDto.builder()
                .isbn("asdk123hksjdh")
                .title("Orlovi rano lete")
                .author(author)
                .build();
    }
    public static BookEntity createTestBookB(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("afbdfhaisfas")
                .title("Vrema Zla")
                .author(author)
                .build();
    }
    public static BookEntity createTestBookC(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("jhdhsfsadfdg")
                .title("Maratonci trce pocasni krug")
                .author(author)
                .build();
    }
}
