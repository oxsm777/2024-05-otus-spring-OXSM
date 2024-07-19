package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    private static List<Genre> dbGenres;

    @BeforeAll
    public static void initGenres() {
        dbGenres = IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    @DisplayName("должен загружать список жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJdbc.findAll();
        var expectedGenres = dbGenres;
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список жанров по списку id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var actualGenres = repositoryJdbc.findAllByIds(Set.of(1L, 2L));
        var expectedGenres = dbGenres.stream().limit(2).toList();
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }
}