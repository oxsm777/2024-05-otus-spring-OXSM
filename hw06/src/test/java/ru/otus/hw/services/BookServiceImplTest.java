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
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.services.mappers.AuthorMapperImpl;
import ru.otus.hw.services.mappers.BookMapperImpl;
import ru.otus.hw.services.mappers.GenreMapperImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class,
        BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private static List<BookDTO> books;

    @BeforeAll
    public static void initFirstBook() {
        books = List.of(
                new BookDTO(1L, "BookTitle_1",
                        new AuthorDTO(1L, "Author_1"),
                        List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))),
                new BookDTO(2L, "BookTitle_2",
                        new AuthorDTO(2L, "Author_2"),
                        List.of(new GenreDTO(3L, "Genre_3"), new GenreDTO(4L, "Genre_4"))),
                new BookDTO(3L, "BookTitle_3",
                        new AuthorDTO(3L, "Author_3"),
                        List.of(new GenreDTO(5L, "Genre_5"), new GenreDTO(6L, "Genre_6")))
        );
    }

    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookService.findById(1L);
        assertThat(actualBook).isPresent().get().isEqualTo(books.get(0));
    }

    @Test
    void whenAllBooksFound() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isEqualTo(books);
    }

    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        var actualBook = bookService.insert("new_book", 1L, Set.of(1L, 2L));
        var expectedBook = new BookDTO(4L, "new_book",
                new AuthorDTO(1L, "Author_1"),
                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2")));
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        var actualBook = bookService.update(1L, "new_book", 1L, Set.of(1L, 2L));
        var expectedBook = new BookDTO(1L, "new_book",
                new AuthorDTO(1L, "Author_1"),
                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2")));
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    @DirtiesContext
    void shouldDeleteBook() {
        bookService.deleteById(1L);
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isEqualTo(List.of(books.get(1), books.get(2)));
    }

}