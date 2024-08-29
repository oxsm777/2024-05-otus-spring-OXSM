package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDTO comment) {
        return "Id: %s, text: %s, bookId: %s".formatted(
                comment.getId(),
                comment.getText(),
                bookConverter.bookToString(comment.getBook()));
    }
}
