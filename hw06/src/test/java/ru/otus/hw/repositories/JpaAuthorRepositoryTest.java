package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = repositoryJpa.findAll();
        assertThat(actualAuthors).hasSize(3);
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = repositoryJpa.findById(1L);
        var expectedAuthor = em.find(Author.class, 1L);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

}