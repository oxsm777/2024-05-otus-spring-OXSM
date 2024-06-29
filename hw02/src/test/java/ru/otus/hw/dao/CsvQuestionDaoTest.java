package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    private static Properties properties;

    @BeforeAll
    public static void init() throws IOException {
        properties = new Properties();
        properties.load(CsvQuestionDaoTest.class.getResourceAsStream("/application.properties"));
    }

    @Test
    void whenAllFound() {
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);
        String fileName = properties.getProperty("test.fileName");
        when(fileNameProvider.getTestFileName()).thenReturn(fileName);
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
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);
        String fileName = properties.getProperty("test.invalidFileName");
        when(fileNameProvider.getTestFileName()).thenReturn(fileName);
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

}