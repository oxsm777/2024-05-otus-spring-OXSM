package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Student;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceImplTest {

    @Test
    void whenDetermineCurrentStudent() {
        LocalizedIOService ioService = mock(LocalizedIOService.class);
        StudentService studentService = new StudentServiceImpl(ioService);
        String firstName = "Feofan";
        String lastName = "Morozov";
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);
        Student student = new Student(firstName, lastName);
        assertEquals(studentService.determineCurrentStudent(), student);
    }

    @Test
    void whenDetermineCurrentStudentFailed() {
        LocalizedIOService ioService = mock(LocalizedIOService.class);
        StudentService studentService = new StudentServiceImpl(ioService);
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