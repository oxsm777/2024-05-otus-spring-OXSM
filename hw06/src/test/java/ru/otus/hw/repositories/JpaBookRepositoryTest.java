package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = repositoryJpa.findById(1L);
        var expectedBook = em.find(Book.class, 1L);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJpa.findAll();
        var expectedBooks = List.of(
                em.find(Book.class, 1L),
                em.find(Book.class, 2L),
                em.find(Book.class, 3L)
        );
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var actualBook = repositoryJpa.save(createNewBook());
        em.persist(createNewBook());
        var expectedBook = em.find(Book.class, 4L);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененнения в книге ")
    @Test
    void shouldUpdateBook() {
        var actualBook = repositoryJpa.save(updateBook());
        em.merge(updateBook());
        var expectedBook = em.find(Book.class, 1L);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        repositoryJpa.deleteById(1L);
        var actualBooks = repositoryJpa.findAll();
        var expectedBooks = List.of(
                em.find(Book.class, 2L),
                em.find(Book.class, 3L)
        );
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    private Book createNewBook() {
        return new Book(0, "BookTitle_4",
                new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
    }

    private Book updateBook() {
        return new Book(1L, "new_title",
                new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
    }

}