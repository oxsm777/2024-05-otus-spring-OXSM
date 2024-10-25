package ru.otus.hw.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.RequestBookDTO;

import java.util.List;
import java.util.Map;

@Component
public class RequestBodyInitializer {

    private final ObjectMapper mapper;

    @Getter
    private Map<String, String> requestBodies;

    public RequestBodyInitializer(ObjectMapper mapper) throws JsonProcessingException {
        this.mapper = mapper;
        initRequestBodies();
    }

    private void initRequestBodies() throws JsonProcessingException {
        requestBodies = Map.of(
                "new-book", mapper.writeValueAsString(new RequestBookDTO(0, "New Book", 2L, List.of(2L))),
                "edit-book", mapper.writeValueAsString(new RequestBookDTO(1L,"New Title", 2L, List.of(1L))),
                "new-comment", mapper.writeValueAsString(new CommentDTO(0, "New Text", 2L)),
                "edit-comment", mapper.writeValueAsString(new CommentDTO(1L,"New Text", 2L))
        );
    }
}
