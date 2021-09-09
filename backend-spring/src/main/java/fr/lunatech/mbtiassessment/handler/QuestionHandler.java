package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.error.domain.ExceptionConstants.QUESTION_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Tag(name = "Question", description = "Endpoints for operations on Questions")
public class QuestionHandler {

    private final static NotFoundException NOT_FOUND_EXCEPTION =
            new NotFoundException(QUESTION_NOT_FOUND);
    private final QuestionService service;
    private final RequestHandler requestHandler;

    public QuestionHandler(QuestionService service, RequestHandler requestHandler) {
        this.service = service;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> getAllQuestions(ServerRequest request) {
        return this.service.getAllQuestions()
                .collectList()
                .flatMap(questionDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getQuestionById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getQuestionById(id)
                .flatMap(questionDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getQuestionnaire(ServerRequest request) {
        return this.service.getQuestionnaire()
                .collectList()
                .flatMap(questionDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getQuestionsByFactor(ServerRequest request) {
        String factorName = request.pathVariable("factor");
        return this.service.getQuestionsByFactor(factorName)
                .collectList()
                .flatMap(questionDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> createQuestion(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(questionDTO -> {
                    questionDTO.setId(null);
                    return this.service.addQuestion(questionDTO);
                })
                .flatMap(questionDTO -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, QuestionDTO.class);
    }

    public @NonNull Mono<ServerResponse> updateQuestion(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(this.service::updateQuestion)
                .flatMap(questionDTO -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(questionDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, QuestionDTO.class);
    }

    public @NonNull Mono<ServerResponse> deleteQuestion(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getQuestionById(id)
                .flatMap(existingQuestion ->
                        ServerResponse.ok()
                                .build(this.service.deleteQuestion(existingQuestion))
                )
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> deleteAllQuestions(ServerRequest request) {
        return ServerResponse.ok()
                .build(this.service.deleteAllQuestions());
    }

}
