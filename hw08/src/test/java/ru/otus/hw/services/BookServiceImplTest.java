package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.mappers.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class,
        CommentServiceImpl.class, CommentMapperImpl.class, BookMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BookMapper bookMapper;

    private static List<BookDTO> bookDTOs;

    @BeforeAll
    public static void initBookDTOs() {
        bookDTOs = List.of(
                new BookDTO("1", "Book_1",
                        new AuthorDTO("1", "Author_1"),
                        List.of(new GenreDTO("1", "Genre_1"))),
                new BookDTO("2", "Book_2",
                        new AuthorDTO("2", "Author_2"),
                        List.of(new GenreDTO("2", "Genre_2"), new GenreDTO("3", "Genre_3")))
        );
    }

    @Test
    void shouldReturnCorrectBookById() {
        Optional<BookDTO> actualBookDTO = bookService.findById("1");
        assertThat(actualBookDTO).isPresent().get().isEqualTo(bookDTOs.get(0));
    }

    @Test
    void whenAllBooksFound() {
        List<BookDTO> actualBookDTOs = bookService.findAll();
        assertThat(actualBookDTOs).isEqualTo(bookDTOs);
    }

    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        bookService.insert("new_book", "1", Set.of("1"));
        List<BookDTO> actualBookDTOs = bookMapper.toDto(mongoOperations.findAll(Book.class));
        assertThat(actualBookDTOs.size()).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    void shouldUpdateBookAndComments() {
        BookDTO actualBookDTO = bookService.update("1", "new_book", "1", Set.of("1"));
        BookDTO expectedBookDTO = new BookDTO("1", "new_book",
                new AuthorDTO("1", "Author_1"),
                List.of(new GenreDTO("1", "Genre_1")));
        assertThat(actualBookDTO).isEqualTo(expectedBookDTO);
        CommentDTO actualCommentDTO = commentMapper.toDto(mongoOperations.findById("1", Comment.class));
        CommentDTO expectedCommentDTO = new CommentDTO("1", "Comment_1", expectedBookDTO);
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @Test
    @DirtiesContext
    void shouldDeleteBook() {
        bookService.deleteById("1");
        List<BookDTO> actualBookDTOs = bookMapper.toDto(mongoOperations.findAll(Book.class));
        assertThat(actualBookDTOs).isEqualTo(List.of(bookDTOs.get(1)));
    }

}