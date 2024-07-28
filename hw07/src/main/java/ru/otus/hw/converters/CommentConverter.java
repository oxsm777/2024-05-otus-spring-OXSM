package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDTO comment) {
        return "Id: %d, text: %s, bookId: %d".formatted(
                comment.getId(),
                comment.getText(),
                comment.getBookId());
    }
}
