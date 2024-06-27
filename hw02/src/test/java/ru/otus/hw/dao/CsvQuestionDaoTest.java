package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.PropertiesConfigTest;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(PropertiesConfigTest.class)
class CsvQuestionDaoTest {

    @Value("${test.rightAnswersCountToPass}")
    private int rightAnswersCountToPass;

    @Value("${test.fileName}")
    private String testFileName;

    @Value("${test.invalidFileName}")
    private String invalidFileName;

    @Test
    void whenAllFound() {
        TestFileNameProvider fileNameProvider = new AppProperties(rightAnswersCountToPass, testFileName);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);
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
        TestFileNameProvider fileNameProvider = new AppProperties(rightAnswersCountToPass, invalidFileName);
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

}