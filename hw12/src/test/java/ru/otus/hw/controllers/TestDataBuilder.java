package ru.otus.hw.controllers;

import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

public class TestDataBuilder {

    private TestDataBuilder() {
    }

    public static Stream<Arguments> getTestData() {
        var roleUser = new String[] {"USER"};
        var roleAdmin = new String[] {"ADMIN"};
        return Stream.of(
                Arguments.of("get", "/api/authors", null, null, 302, true, null),
                Arguments.of("get", "/api/authors", "user", roleUser, 403, false, null),
                Arguments.of("get", "/api/authors", "admin", roleAdmin, 200, false, null),

                Arguments.of("get", "/api/genres", null, null, 302, true, null),
                Arguments.of("get", "/api/genres", "user", roleUser, 403, false, null),
                Arguments.of("get", "/api/genres", "admin", roleAdmin, 200, false, null),

                Arguments.of("get", "/api/books", null, null, 302, true, null),
                Arguments.of("get", "/api/books", "user", roleUser, 200, false, null),
                Arguments.of("get", "/api/books", "admin", roleAdmin, 200, false, null),
                Arguments.of("get", "/api/books/1", null, null, 302, true, null),
                Arguments.of("get", "/api/books/1", "user", roleUser, 403, false, null),
                Arguments.of("get", "/api/books/1", "admin", roleAdmin, 200, false, null),
                Arguments.of("post", "/api/books", null, null, 302, true, "new-book"),
                Arguments.of("post", "/api/books", "user", roleUser, 200, false, "new-book"),
                Arguments.of("post", "/api/books", "admin", roleAdmin, 200, false, "new-book"),
                Arguments.of("put", "/api/books", null, null, 302, true, "edit-book"),
                Arguments.of("put", "/api/books", "user", roleUser, 200, false, "edit-book"),
                Arguments.of("put", "/api/books", "admin", roleAdmin, 200, false, "edit-book"),
                Arguments.of("delete", "/api/books/1", null, null, 302, true, null),
                Arguments.of("delete", "/api/books/1", "user", roleUser, 403, false, null),
                Arguments.of("delete", "/api/books/1", "admin", roleAdmin, 200, false, null),

                Arguments.of("get", "/api/comments?bookId=1", null, null, 302, true, null),
                Arguments.of("get", "/api/comments?bookId=1", "user", roleUser, 200, false, null),
                Arguments.of("get", "/api/comments?bookId=1", "admin", roleAdmin, 200, false, null),
                Arguments.of("get", "/api/comments/1", null, null, 302, true, null),
                Arguments.of("get", "/api/comments/1", "user", roleUser, 403, false, null),
                Arguments.of("get", "/api/comments/1", "admin", roleAdmin, 200, false, null),
                Arguments.of("post", "/api/comments", null, null, 302, true, "new-comment"),
                Arguments.of("post", "/api/comments", "user", roleUser, 200, false, "new-comment"),
                Arguments.of("post", "/api/comments", "admin", roleAdmin, 200, false, "new-comment"),
                Arguments.of("put", "/api/comments", null, null, 302, true, "edit-comment"),
                Arguments.of("put", "/api/comments", "user", roleUser, 200, false, "edit-comment"),
                Arguments.of("put", "/api/comments", "admin", roleAdmin, 200, false, "edit-comment"),
                Arguments.of("delete", "/api/comments/1", null, null, 302, true, null),
                Arguments.of("delete", "/api/comments/1", "user", roleUser, 403, false, null),
                Arguments.of("delete", "/api/comments/1", "admin", roleAdmin, 200, false, null)
        );
    }
}
