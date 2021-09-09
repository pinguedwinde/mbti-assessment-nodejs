package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedFactorException;
import fr.lunatech.mbtiassessment.service.QuestionService;
import fr.lunatech.mbtiassessment.test.model.QuestionTestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.QUESTION_BASE_URL;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionRouterTest {

    private final String undefinedFactor;
    private final String invalidIdNotFound;
    private WebTestClient client;
    @MockBean
    private QuestionService service;
    @LocalServerPort
    private int port;
    private List<QuestionDTO> questionDTOS;
    private QuestionDTO assertive;
    private QuestionDTO introversion;
    private QuestionDTO feeling;

    public QuestionRouterTest() {
        this.invalidIdNotFound = "Question not found cause by either bad path or invalid ID";
        this.undefinedFactor = "Undefined Personality Factor";
    }

    @BeforeEach
    public void setup() {
        this.client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + QUESTION_BASE_URL)
                .build();
        this.assertive = new QuestionDTO("78c3c426", "You rarely feel insecure.", "assertive");
        this.introversion = new QuestionDTO("e00fe410", "You avoid making phone calls.", "introversion");
        this.feeling = new QuestionDTO("d6964805", "You are very sentimental.", "feeling");
        this.questionDTOS = List.of(this.assertive, this.introversion, this.feeling);
    }

    @Test
    public void testGetAllQuestion() {
        Mockito.when(this.service.getAllQuestions()).thenReturn(Flux.fromIterable(questionDTOS));
        this.client
                .get()
                .uri("")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(QuestionDTO.class)
                .hasSize(questionDTOS.size());
    }

    @Test
    public void testReturnJsonWhenGetQuestionById() {
        String id = "78c3c426";
        Mockito.when(this.service.getQuestionById(id)).thenReturn(Mono.just(this.assertive));
        this.client
                .get()
                .uri("/id/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.content").isEqualTo(this.assertive.getContent());
    }

    @Test
    public void testGetQuestionByIdWithInvalidIdNotFound() {
        String id = "78c3c426-error";
        Mockito.when(this.service.getQuestionById(id)).thenReturn(Mono.empty());
        this.client
                .get()
                .uri("/id/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void testGetQuestionByFactor() {
        Mockito.when(this.service.getQuestionsByFactor("introversion"))
                .thenReturn(Flux.just(this.introversion, this.introversion));
        this.client
                .get()
                .uri("/factor/{factor}", "introversion")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(this.introversion.getId())
                .jsonPath("$[0].content").isEqualTo(this.introversion.getContent());
    }

    @Test
    public void testGetQuestionByUndefinedFactor() {
        Mockito.when(this.service.getQuestionsByFactor("introversion-with-error"))
                .thenReturn(Flux.error(UndefinedFactorException::new));
        this.client
                .get()
                .uri("/factor/{factor}", "introversion-with-error")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(QUESTION_BASE_URL + "/factor/introversion-with-error")
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.code").isEqualTo("400")
                .jsonPath("$.message").isEqualTo(this.undefinedFactor);
    }

    @Test
    public void testCreateQuestion() {
        String id = "6081546c2209146d12bb0d19";
        String factorName = "turbulent";
        String content = "Your mood can change very quickly.";
        QuestionTestDTO questionDTO = new QuestionTestDTO(content, factorName);
        QuestionDTO returnedQuestionDTO = new QuestionDTO(id, content, factorName);
        Mockito.when(this.service.addQuestion(any(QuestionDTO.class)))
                .thenReturn(Mono.just(returnedQuestionDTO));
        this.client
                .post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(questionDTO))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.content").isEqualTo(questionDTO.getContent());
    }

    @Test
    public void testCreateQuestionWithUndefinedFactor() {
        String factorName = "sensing-with-error";
        String content = "You are rarely distracted by your dreams and thoughts.";
        QuestionTestDTO questionDTO = new QuestionTestDTO(content, factorName);
        Mockito.when(this.service.addQuestion(any(QuestionDTO.class))).thenReturn(Mono.error(UndefinedFactorException::new));
        this.client
                .post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(questionDTO))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(QUESTION_BASE_URL)
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.code").isEqualTo("400")
                .jsonPath("$.message").isEqualTo(this.undefinedFactor);
    }

    @Test
    public void testUpdateQuestion() {
        String id = "78c3c426";
        String factorName = "sensing";
        String content = "You are rarely distracted by your dreams and thoughts.";
        String updatedFactorName = "intuition";
        String updatedContent = content + " Updated!";
        QuestionTestDTO questionDTO = new QuestionTestDTO(id, content, factorName);
        QuestionDTO returnedQuestionDTO = new QuestionDTO(id, updatedContent, updatedFactorName);
        Mockito.when(this.service.updateQuestion(any(QuestionDTO.class))).thenReturn(Mono.just(returnedQuestionDTO));
        this.client
                .put()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(questionDTO))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.content").isEqualTo(updatedContent);
    }

    @Test
    public void testUpdateQuestionWithUndefinedFactor() {
        String id = "78c3c426";
        String factorName = "sensing-with-error";
        String content = "You are rarely distracted by your dreams and thoughts.";
        QuestionTestDTO questionDTO = new QuestionTestDTO(id, content, factorName);
        Mockito.when(this.service.updateQuestion(any(QuestionDTO.class))).thenReturn(Mono.error(UndefinedFactorException::new));
        this.client
                .put()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(questionDTO))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(QUESTION_BASE_URL + "/" + id)
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.code").isEqualTo("400")
                .jsonPath("$.message").isEqualTo(this.undefinedFactor);
    }

    @Test
    public void testUpdateQuestionInvalidIdNotFound() {
        String id = "78c3c426-with-error";
        String factorName = "sensing";
        String content = "You are rarely distracted by your dreams and thoughts.";
        QuestionTestDTO questionDTO = new QuestionTestDTO(id, content, factorName);
        Mockito.when(this.service.updateQuestion(any(QuestionDTO.class))).thenReturn(Mono.empty());
        this.client
                .put()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(questionDTO))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(QUESTION_BASE_URL + "/" + id)
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.code").isEqualTo("404")
                .jsonPath("$.message").isEqualTo(this.invalidIdNotFound);
    }

    @Test
    public void testDeleteQuestion() {
        String id = "d6964805";
        Mockito.when(this.service.getQuestionById(id)).thenReturn(Mono.just(this.feeling));
        Mockito.when(this.service.deleteQuestion(this.feeling)).thenReturn(Mono.empty());
        this.client
                .delete()
                .uri("/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void testDeleteQuestionInvalidIdNotFound() {
        String id = "78c3c426-with-error";
        Mockito.when(this.service.getQuestionById(id)).thenReturn(Mono.empty());
        this.client
                .delete()
                .uri("/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(QUESTION_BASE_URL + "/" + id)
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.code").isEqualTo("404")
                .jsonPath("$.message").isEqualTo(this.invalidIdNotFound);
    }
}
