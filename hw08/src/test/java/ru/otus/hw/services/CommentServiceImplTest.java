package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.mappers.AuthorMapperImpl;
import ru.otus.hw.services.mappers.BookMapperImpl;
import ru.otus.hw.services.mappers.CommentMapperImpl;
import ru.otus.hw.services.mappers.GenreMapperImpl;

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

    private static List<CommentDTO> commentDTOs;

    @BeforeAll
    public static void initCommentDTOs() {
        commentDTOs = List.of(
                new CommentDTO("1", "Comment_1", "1"),
                new CommentDTO("2", "Comment_2", "2")
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
        List<CommentDTO> actualCommentDTOs = commentService.findAllByBookId("1");
        assertThat(actualCommentDTOs.size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        CommentDTO actualCommentDTO = commentService.update("1", "new_text", "1");
        CommentDTO expectedCommentDTO = new CommentDTO("1", "new_text", "1");
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        commentService.deleteById("1");
        List<CommentDTO> actualCommentDTOs = commentService.findAllByBookId("1");
        assertThat(actualCommentDTOs).isEqualTo(Collections.EMPTY_LIST);
    }

}