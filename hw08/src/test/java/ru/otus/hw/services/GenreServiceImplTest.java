package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.mappers.GenreMapperImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import({GenreServiceImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @Test
    void whenAllGenresFound() {
        List<GenreDTO> expectedGenreDTOs = List.of(
                new GenreDTO("1", "Genre_1"),
                new GenreDTO("2", "Genre_2"),
                new GenreDTO("3", "Genre_3")
        );
        List<GenreDTO> actualGenreDTOs = genreService.findAll();
        assertEquals(expectedGenreDTOs, actualGenreDTOs);
    }
}