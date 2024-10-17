package ru.otus.hw.services;

import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.RequestBookDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<BookDTO> findById(long id);

    List<BookDTO> findAll();

    BookDTO insert(String title, long authorId, Set<Long> genresIds);

    BookDTO update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);

    Optional<RequestBookDTO> findBookById(long id);
}
