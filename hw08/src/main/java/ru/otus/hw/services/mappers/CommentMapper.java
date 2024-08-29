package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

}
