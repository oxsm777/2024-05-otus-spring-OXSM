package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CsvQuestionDao.class})
class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void whenAllFound() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        var expected = List.of(
                new Question("2*2=?",
                        List.of(new Answer("4", true),
                                new Answer("1", false),
                                new Answer("3", false))),
                new Question("5+5=?",
                        List.of(new Answer("10", true),
                                new Answer("8", false)))
        );
        assertEquals(csvQuestionDao.findAll(), expected);
    }

    @Test
    void whenTryToReadFileThatNotExists() {
        when(fileNameProvider.getTestFileName()).thenReturn("not.csv");
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

}