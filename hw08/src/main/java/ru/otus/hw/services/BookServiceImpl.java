package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mappers.BookMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentService commentService;

    private final BookMapper bookMapper;

    @Override
    public Optional<BookDTO> findById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookMapper::toDto);
    }

    @Override
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDto(books);
    }

    @Override
    @Transactional
    public BookDTO insert(String title, String authorId, Set<String> genresIds) {
        return bookMapper.toDto(save(null, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDTO update(String id, String title, String authorId, Set<String> genresIds) {
        Book book = save(id, title, authorId, genresIds);
        commentService.updateAllCommentsByBook(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        bookRepository.deleteById(id);
        commentRepository.deleteByBookId(id);
    }

    private Book save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }

}
