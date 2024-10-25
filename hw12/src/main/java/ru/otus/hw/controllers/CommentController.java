package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments(@RequestParam long bookId) {
        List<CommentDTO> comments = commentService.findAllByBookId(bookId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable long id) {
        CommentDTO comment = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/api/comments")
    public ResponseEntity<Void> insertComment(@RequestBody CommentDTO comment) {
        commentService.insert(comment.getText(), comment.getBookId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/comments")
    public ResponseEntity<Void> updateComment(@RequestBody CommentDTO comment) {
        commentService.update(comment.getId(), comment.getText(), comment.getBookId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
