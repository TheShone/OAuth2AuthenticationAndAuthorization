package com.starting.database.contollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starting.database.TestDataUtil;
import com.starting.database.domain.dto.AuthorDto;
import com.starting.database.domain.entities.AuthorEntity;
import com.starting.database.services.AuthorService;
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

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .with(jwt().jwt(jwt->{
                            jwt.claims(claims->{
                                claims.put("scope","message-read");
                                claims.put("scope","message-write");
                            }).subject("oidc-client")
                                    .notBefore(Instant.now().minusSeconds(5l));
                        }))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
        );
    }
    @Test
    public void testThatGetAuthorsSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatGetAuthorsReturnListOfAuthors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Nenad Pavlovic")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(23)
        );
    }
    @Test
    public void testThatGetAuthorReturnsHttp200Ok() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        authorService.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        author.setId(null);
        authorService.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()));
    }
    @Test
    public void testThatUpdateAuthorSuccessfullyReturnsHttp200Ok() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testThatUpdateAuthorSuccessfullyReturnsUpdatedUser() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        String authorJson = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()));
    }
    @Test
    public void testThatDeleteAuthorSuccessfullyReturnsHttp204Ok() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(author);
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
