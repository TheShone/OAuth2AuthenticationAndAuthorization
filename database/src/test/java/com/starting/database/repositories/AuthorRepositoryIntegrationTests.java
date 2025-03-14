 package com.starting.database.repositories;

import com.starting.database.TestDataUtil;
import com.starting.database.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTests {

   private AuthorRepository underTest;

   @Autowired
   public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
       this.underTest = underTest;
   }

   @Test
   public void testThatAuthorCanBeCreatedAndRetrieved() {
       AuthorEntity author = TestDataUtil.createTestAuthorA();
       author.setId(null);
       underTest.save(author);
       Optional<AuthorEntity> result =underTest.findById(author.getId());
       assertThat(result).isPresent();
       assertThat(result.get()).isEqualTo(author);
   }


 @Test
   public void testThatAuthorsCanBeRetrieved() {
       AuthorEntity author = TestDataUtil.createTestAuthorA();
       author.setId(null);
       underTest.save(author);
       AuthorEntity author2 = TestDataUtil.createTestAuthorB();
       author2.setId(null);
       underTest.save(author2);
       AuthorEntity author3 = TestDataUtil.createTestAuthorC();
       author3.setId(null);
       underTest.save(author3);
       Iterable<AuthorEntity> authors = underTest.findAll();
       assertThat(authors)
               .contains(author2, author3, author);
   }

   @Test
   public void testThatAuthorsCanBeUpdated() {
       AuthorEntity author = TestDataUtil.createTestAuthorA();
       author.setId(null);
       underTest.save(author);
       author.setName("Dragan");
       underTest.save(author);
       Optional<AuthorEntity> result =underTest.findById(author.getId());
       assertThat(result)
               .isPresent();
       assertThat(result.get()).isEqualTo(author);

   }

   @Test
   public void testThatAuthorsCanBeDeleted() {
       AuthorEntity author = TestDataUtil.createTestAuthorA();
       author.setId(null);
       underTest.save(author);
       underTest.deleteById(author.getId());
       Optional<AuthorEntity> result =underTest.findById(author.getId());
       assertThat(result).isNotPresent();
   }

   @Test
    public void testThatGetAuthorsWithAgeLessThan() {
       AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
       testAuthorA.setId(null);
       underTest.save(testAuthorA);
       AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
       testAuthorB.setId(null);
       underTest.save(testAuthorB);
       AuthorEntity testAuthorC = TestDataUtil.createTestAuthorC();
       testAuthorC.setId(null);
       underTest.save(testAuthorC);
       List<AuthorEntity> result = underTest.findAuthorByAgeBefore(81);
       assertThat(result).hasSize(26);
   }

   //@Test
    //public void testThatGetAuthorsWithAgeGreaterThan() {
      // Iterable<AuthorEntity> results= underTest.findAuthorsWithAgeGreaterThan(81);
       //assertThat(results).hasSize(28);
   //}

}
