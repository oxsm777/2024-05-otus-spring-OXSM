package ru.otus.hw.controllers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, RequestBodyInitializer.class})
@WebMvcTest(controllers = {BookController.class, CommentController.class, AuthorController.class, GenreController.class})
public class ControllersSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    private final BookDTO bookDTO = new BookDTO(1L, "Test Book", new AuthorDTO(1L, "Author Name"), new ArrayList<>());

    private final CommentDTO commentDTO = new CommentDTO(1L, "Text", 1L);

    @Autowired
    private RequestBodyInitializer requestBodyInitializer;

    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status")
    @MethodSource("getTestData")
    @DirtiesContext
    void shouldReturnExpectedStatus(String method, String url, String username, String[] roles, int status,
                                    boolean checkLoginRedirection, String requestBody) throws Exception {
        var request = method2RequestBuilder(method, url, requestBody);
        if (nonNull(username)) {
            request = request.with(user(username).roles(roles));
        }
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDTO));
        when(commentService.findById(1L)).thenReturn(Optional.of(commentDTO));
        ResultActions resultActions = mockMvc.perform(request).andExpect(status().is(status));
        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url, String requestBody) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post,
                "put", MockMvcRequestBuilders::put,
                "delete", MockMvcRequestBuilders::delete
        );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = methodMap.get(method).apply(url);
        return nonNull(requestBody)
                ? mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyInitializer.getRequestBodies().get(requestBody))
                : mockHttpServletRequestBuilder;
    }

    public static Stream<Arguments> getTestData() {
        return TestDataBuilder.getTestData();
    }

}
