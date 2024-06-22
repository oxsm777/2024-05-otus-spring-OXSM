package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

class CsvQuestionDaoTest {

    @Test
    void whenAllFound() {
        TestFileNameProvider fileNameProvider = new AppProperties("questions.csv");
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
        Assertions.assertEquals(csvQuestionDao.findAll(), expected);
    }

    @Test
    void whenTryToReadFileThatNotExists() {
        TestFileNameProvider fileNameProvider = new AppProperties("not.csv");
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);
        Assertions.assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

}