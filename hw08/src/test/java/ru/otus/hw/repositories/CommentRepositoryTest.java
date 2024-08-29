package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами ")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = repository.findByBookId("1");
        var expectedComments = List.of(
                new Comment("1", "Comment_1",
                        new Book("1", "Book_1",
                                new Author("1", "Author_1"),
                                List.of(new Genre("1", "Genre_1"))))
        );
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @DisplayName("должен удалить список комментариев по id книги")
    @Test
    @DirtiesContext
    void shouldDeleteCommentsByBookId() {
        repository.deleteByBookId("1");
        var expectedComments = List.of(
                new Comment("2", "Comment_2",
                        new Book("2", "Book_2",
                                new Author("2", "Author_2"),
                                List.of(new Genre("2", "Genre_2"), new Genre("3", "Genre_3"))))
        );
        assertThat(mongoOperations.findAll(Comment.class)).isEqualTo(expectedComments);
    }
}