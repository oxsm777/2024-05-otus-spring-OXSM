package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.services.mappers.AuthorMapperImpl;
import ru.otus.hw.services.mappers.BookMapperImpl;
import ru.otus.hw.services.mappers.CommentMapperImpl;
import ru.otus.hw.services.mappers.GenreMapperImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentServiceImpl.class, JpaCommentRepository.class, JpaBookRepository.class, JpaAuthorRepository.class,
        JpaGenreRepository.class,
        CommentMapperImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    private static List<CommentDTO> comments;

    @BeforeAll
    public static void initFirstBook() {
        comments = List.of(
                new CommentDTO(1L, "text_1",
                        new BookDTO(1L, "BookTitle_1",
                                new AuthorDTO(1L, "Author_1"),
                                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2")))),
                new CommentDTO(2L, "text_2",
                        new BookDTO(2L, "BookTitle_2",
                                new AuthorDTO(2L, "Author_2"),
                                List.of(new GenreDTO(3L, "Genre_3"), new GenreDTO(4L, "Genre_4")))),
                new CommentDTO(3L, "text_3",
                        new BookDTO(1L, "BookTitle_1",
                                new AuthorDTO(1L, "Author_1"),
                                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))))
        );
    }

    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentService.findById(1L);
        assertThat(actualComment).isPresent().get().isEqualTo(comments.get(0));
    }

    @Test
    void whenAllCommentsByBooksIdFound() {
        var actualComments = commentService.findAllByBookId(1L);
        assertThat(actualComments).isEqualTo(List.of(comments.get(0), comments.get(2)));
    }

    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var actualComment = commentService.insert("new_text", 1L);
        var expectedComment = new CommentDTO(4L, "new_text",
                new BookDTO(1L, "BookTitle_1",
                        new AuthorDTO(1L, "Author_1"),
                        List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))));
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        var actualComment = commentService.update(1L, "new_text", 1L);
        var expectedComment = new CommentDTO(1L, "new_text",
                new BookDTO(1L, "BookTitle_1",
                new AuthorDTO(1L, "Author_1"),
                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))));
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        commentService.deleteById(1L);
        var actualComments = commentService.findAllByBookId(1L);
        assertThat(actualComments).isEqualTo(List.of(comments.get(2)));
    }

}