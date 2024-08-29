package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать список жанров по списку id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var actualGenres = repository.findByIdIn(Set.of("1", "2"));
        var expectedGenres = List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2"));
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

}