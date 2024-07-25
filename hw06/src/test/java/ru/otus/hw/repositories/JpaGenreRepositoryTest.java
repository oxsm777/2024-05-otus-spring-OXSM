package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repositoryJpa;

    @DisplayName("должен загружать список жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJpa.findAll();
        assertThat(actualGenres).hasSize(6);
    }

    @DisplayName("должен загружать список жанров по списку id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var actualGenres = repositoryJpa.findAllByIds(Set.of(1L, 5L));
        var expectedGenres = List.of(new Genre(1L, "Genre_1"), new Genre(5L, "Genre_5"));
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

}