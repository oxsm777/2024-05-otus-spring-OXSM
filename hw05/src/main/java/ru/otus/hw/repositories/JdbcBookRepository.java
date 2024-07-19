package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
                SELECT b.id, b.title, a.id AS author_id, a.full_name AS author_full_name, 
                g.id AS genre_id, g.name AS genre_name 
                FROM books b 
                JOIN authors a ON b.author_id = a.id 
                LEFT JOIN books_genres bg ON b.id = bg.book_id 
                LEFT JOIN genres g ON bg.genre_id = g.id 
                WHERE b.id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Book book = jdbcTemplate.query(sql, params, new BookResultSetExtractor());
        return Objects.isNull(book) ? Optional.empty() : Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM books WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        String sql = "SELECT id, title, author_id FROM books";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());
        setAuthorsForBooks(books);
        return books;
    }

    private void setAuthorsForBooks(List<Book> books) {
        Map<Long, Author> authorsByIds = getAuthorsByIds();
        books.forEach(book -> book.setAuthor(authorsByIds.get(book.getAuthor().getId())));
    }

    private Map<Long, Author> getAuthorsByIds() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(toMap(Author::getId, author -> author));
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        String sql = "SELECT book_id, genre_id FROM books_genres";
        return jdbcTemplate.query(sql, new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, List<Genre>> genresByBookId = groupGenresByBookId(genres, relations);
        booksWithoutGenres.forEach(book -> book.setGenres(genresByBookId.get(book.getId())));
    }

    private Map<Long, List<Genre>> groupGenresByBookId(List<Genre> genres, List<BookGenreRelation> relations) {
        Map<Long, Genre> genresById = this.getGenresById(genres);
        return relations.stream()
                .collect(groupingBy(
                        BookGenreRelation::bookId,
                        mapping(relation -> genresById.get(relation.genreId()), toList())
                ));
    }

    private Map<Long, Genre> getGenresById(List<Genre> genres) {
        return genres.stream().collect(toMap(Genre::getId, genre -> genre));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO books (title, author_id) VALUES (:title, :author_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId());
        jdbcTemplate.update(sql, params, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        String sql = "UPDATE books SET title = :title, author_id = :author_id WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("id", book.getId());
        int updatedRows = jdbcTemplate.update(sql, params);
        if (updatedRows == 0) {
            throw new EntityNotFoundException(String.format("Book with id %s not found", book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        String sql = "INSERT INTO books_genres (book_id, genre_id) VALUES (:book_id, :genre_id)";
        SqlParameterSource[] params = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource("book_id", book.getId())
                .addValue("genre_id", genre.getId()))
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(sql, params);
    }

    private void removeGenresRelationsFor(Book book) {
        String sql = "DELETE FROM books_genres WHERE book_id = :book_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("book_id", book.getId());
        jdbcTemplate.update(sql, params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            Author author = new Author(authorId, null);
            return new Book(bookId, title, author, new ArrayList<>());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            BookRowMapper bookRowMapper = new BookRowMapper();
            Book book = null;
            if (rs.next()) {
                book = bookRowMapper.mapRow(rs, rs.getRow());
                String authorFullName = rs.getString("author_full_name");
                book.getAuthor().setFullName(authorFullName);
                book.getGenres().add(getGenreFromResultSet(rs));
                while (rs.next()) {
                    book.getGenres().add(getGenreFromResultSet(rs));
                }
            }
            return book;
        }

        private Genre getGenreFromResultSet(ResultSet rs) throws SQLException {
            return new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
        }
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
