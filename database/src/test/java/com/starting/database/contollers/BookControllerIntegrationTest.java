package com.starting.database.contollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starting.database.TestDataUtil;
import com.starting.database.domain.dto.BookDto;
import com.starting.database.domain.entities.BookEntity;
import com.starting.database.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerIntegrationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreatingBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }
    @Test
    public void testThatCreatingBookSuccessfullyReturnsCreatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthor())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        );

    }

    @Test
    public void testThatGettingBooksSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatGettingBooksSuccessfullyReturnBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value("Hajdi")
        );
    }

    @Test
    public void testThatGettinBooksSuccessfullyReturnsHttp200Ok() throws Exception {
        BookEntity testBook = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + testBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatGettinBooksSuccessfullyReturnsBook() throws Exception {
        BookEntity testBook = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + testBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBook.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateBookSuccessfullyReturnsHttp200Ok() throws Exception {
        BookEntity testBook = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBook.getIsbn(), testBook);
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setTitle("Nista Spec");
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + testBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
