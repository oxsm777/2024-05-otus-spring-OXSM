package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import({JdbcAuthorRepository.class})
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repositoryJdbc;

    private static List<Author> dbAuthors;

    @BeforeAll
    public static void initAuthors() {
        dbAuthors = IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    @DisplayName("должен загружать список авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        var actualAuthors = repositoryJdbc.findAll();
        var expectedAuthors = dbAuthors;
        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = repositoryJdbc.findById(1L);
        var expectedAuthor = dbAuthors.get(0);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }
}