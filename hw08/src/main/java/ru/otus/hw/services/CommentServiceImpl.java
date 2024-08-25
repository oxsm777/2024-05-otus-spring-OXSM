package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.mappers.CommentMapper;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public Optional<CommentDTO> findById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(commentMapper::toDto);
    }

    @Override
    public List<CommentDTO> findAllByBookId(String bookId) {
        return commentMapper.toDto(commentRepository.findByBookId(bookId));
    }

    @Override
    @Transactional
    public CommentDTO insert(String text, String bookId) {
        return commentMapper.toDto(save(null, text, bookId));
    }

    @Override
    @Transactional
    public CommentDTO update(String id, String text, String bookId) {
        return commentMapper.toDto(save(id, text, bookId));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        commentRepository.deleteById(id);
    }

    @Override
    public void updateAllCommentsByBook(Book book) {
        List<Comment> comments = commentRepository.findByBookId(book.getId());
        if (!isEmpty(comments)) {
            comments.forEach(comment -> comment.setBook(book));
            commentRepository.saveAll(comments);
        }
    }

    private Comment save(String id, String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

}
