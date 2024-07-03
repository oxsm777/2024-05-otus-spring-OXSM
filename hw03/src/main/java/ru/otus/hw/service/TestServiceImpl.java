package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            printQuestionAndItsAnswers(question);
            var isAnswerValid = readStudentAnswer(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestionAndItsAnswers(Question question) {
        ioService.printFormattedLine(question.text());
        question.answers().forEach(answer -> ioService.printFormattedLine(answer.text()));
        ioService.printLine("");
    }

    private boolean readStudentAnswer(Question question) {
        var answerNumber = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                "Enter the number of the correct answer", "Invalid input! Try again!");
        return question.answers().get(answerNumber - 1).isCorrect();
    }

}
