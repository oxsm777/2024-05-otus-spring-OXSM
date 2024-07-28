package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaCommentRepository.class, JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = repositoryJpa.findById(1L);
        var expectedComment = em.find(Comment.class, 1L);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = repositoryJpa.findAllByBookId(1L);
        var expectedComments = List.of(
                em.find(Comment.class, 1L),
                em.find(Comment.class, 3L)
        );
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var actualComment = repositoryJpa.save(createNewComment());
        em.persist(createNewComment());
        var expectedComment = em.find(Comment.class, 4L);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененнения в комментарии ")
    @Test
    void shouldUpdateComment() {
        var actualComment = repositoryJpa.save(updateComment());
        em.merge(updateComment());
        var expectedComment = em.find(Comment.class, 1L);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        repositoryJpa.deleteById(1L);
        var actualComments = repositoryJpa.findAllByBookId(1L);
        var expectedComments = List.of(em.find(Comment.class, 3L));
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    private Comment createNewComment() {
        return new Comment (0, "new_text",
                new Book(1L, "BookTitle_1",
                new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"))));
    }

    private Comment updateComment() {
        return new Comment (1L, "updated_text",
                new Book(1L, "BookTitle_1",
                new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"))));
    }
}