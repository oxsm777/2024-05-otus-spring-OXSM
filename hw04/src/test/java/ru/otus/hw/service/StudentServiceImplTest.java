package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentService studentService;

    @Test
    void whenDetermineCurrentStudent() {
        String firstName = "Feofan";
        String lastName = "Morozov";
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);
        Student student = new Student(firstName, lastName);
        assertEquals(studentService.determineCurrentStudent(), student);
    }

    @Test
    void whenDetermineCurrentStudentFailed() {
        String firstName = "Feofan";
        String lastName = "Morozov";
        when(ioService.readStringWithPromptLocalized("Please input your first name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("Please input your last name")).thenReturn(lastName);
        String invalidFirstName = "Stepan";
        String invalidLastName = "Petrov";
        Student student = new Student(invalidFirstName, invalidLastName);
        assertNotEquals(studentService.determineCurrentStudent(), student);
    }

}