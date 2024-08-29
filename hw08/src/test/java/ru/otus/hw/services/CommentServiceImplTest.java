package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.mappers.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({CommentServiceImpl.class,
        CommentMapperImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private CommentMapper commentMapper;

    private static List<CommentDTO> commentDTOs;

    @BeforeAll
    public static void initCommentDTOs() {
        commentDTOs = List.of(
                new CommentDTO("1", "Comment_1",
                        new BookDTO("1", "Book_1",
                                new AuthorDTO("1", "Author_1"),
                                List.of(new GenreDTO("1", "Genre_1")))),
                new CommentDTO("2", "Comment_2",
                        new BookDTO("2", "Book_2",
                                new AuthorDTO("2", "Author_2"),
                                List.of(new GenreDTO("2", "Genre_2"), new GenreDTO("3", "Genre_3"))))
        );
    }

    @Test
    void shouldReturnCorrectCommentById() {
        Optional<CommentDTO> actualCommentDTO = commentService.findById("1");
        assertThat(actualCommentDTO).isPresent().get().isEqualTo(commentDTOs.get(0));
    }

    @Test
    void whenAllCommentsByBooksIdFound() {
        List<CommentDTO> actualCommentDTOs = commentService.findAllByBookId("1");
        assertThat(actualCommentDTOs).isEqualTo(List.of(commentDTOs.get(0)));
    }

    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        commentService.insert("new_text", "1");
        assertThat(findAllCommentsByBookId("1").size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        CommentDTO actualCommentDTO = commentService.update("1", "new_text", "1");
        CommentDTO expectedCommentDTO = new CommentDTO("1", "new_text", new BookDTO("1", "Book_1",
                new AuthorDTO("1", "Author_1"),
                List.of(new GenreDTO("1", "Genre_1"))));
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @Test
    @DirtiesContext
    void shouldUpdateCommentsByBook() {
        Book book = new Book("1", "book_12345",
                new Author("1", "Author_1"),
                List.of(new Genre("1", "Genre_1")));
        commentService.updateAllCommentsByBook(book);
        List<CommentDTO> expectedCommentDTOs = List.of(new CommentDTO("1", "Comment_1", bookMapper.toDto(book)));
        assertThat(findAllCommentsByBookId("1")).isEqualTo(expectedCommentDTOs);
    }

    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        commentService.deleteById("1");
        assertThat(findAllCommentsByBookId("1")).isEqualTo(Collections.EMPTY_LIST);
    }

    private List<CommentDTO> findAllCommentsByBookId(String bookId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("bookId").is(bookId));
        return commentMapper.toDto(mongoOperations.find(query, Comment.class));
    }

}