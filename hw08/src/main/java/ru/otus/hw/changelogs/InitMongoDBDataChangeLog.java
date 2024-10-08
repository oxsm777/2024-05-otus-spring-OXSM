package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author author1;

    private Author author2;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Book book1;

    private Book book2;

    @ChangeSet(order = "000", id = "dropDB", author = "oxsm", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAurhors", author = "oxsm", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("1", "Author_1"));
        author2 = repository.save(new Author("2", "Author_2"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "oxsm", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("1", "Genre_1"));
        genre2 = repository.save(new Genre("2", "Genre_2"));
        genre3 = repository.save(new Genre("3", "Genre_3"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "oxsm", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = repository.save(new Book("1", "Book_1", author1, List.of(genre1)));
        book2 = repository.save(new Book("2", "Book_2", author2, List.of(genre2, genre3)));
    }

    @ChangeSet(order = "004", id = "initComments", author = "oxsm", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment("1", "Comment_1", book1));
        repository.save(new Comment("2", "Comment_2", book2));
    }
}
