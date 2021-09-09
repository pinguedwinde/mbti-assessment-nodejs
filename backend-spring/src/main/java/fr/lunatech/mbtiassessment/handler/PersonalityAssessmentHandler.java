package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.dto.AnswerDTO;
import fr.lunatech.mbtiassessment.error.domain.ExceptionConstants;
import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.service.PersonalityAssessmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Tag(name = "PersonalityAssessment", description = "Endpoints for operations on the Personality Assessment")
public class PersonalityAssessmentHandler {
    private final static NotFoundException NOT_FOUND_EXCEPTION =
            new NotFoundException(ExceptionConstants.PERSONALITY_ASSESSMENT_NOT_FOUND);
    private final PersonalityAssessmentService service;
    private final RequestHandler requestHandler;

    public PersonalityAssessmentHandler(PersonalityAssessmentService service, RequestHandler requestHandler) {
        this.service = service;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> getAllPersonalityAssessments(ServerRequest request) {
        return this.service.getAllPersonalityAssessments()
                .collectList()
                .flatMap(personalityAssessmentDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityAssessmentDTOs)
                );
    }

    public @NonNull Mono<ServerResponse> getPersonalityAssessmentById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getPersonalityAssessmentById(id)
                .flatMap(personalityAssessmentDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityAssessmentDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonalityAssessmentByUsername(ServerRequest request) {
        String username = request.pathVariable("username");
        return this.service.getPersonalityAssessmentByUsername(username)
                .flatMap(personalityAssessmentDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityAssessmentDTO));
    }

    public @NonNull Mono<ServerResponse> processPersonalityAssessment(ServerRequest request) {
        String username = request.pathVariable("username");
        return this.requestHandler.requireValidBodyFlux(answerDTOFlux -> this.service
                .processPersonalityAssessment(answerDTOFlux, username)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(result)
                ), request, AnswerDTO.class);
    }

    public @NonNull Mono<ServerResponse> getPersonageByPersonalityAssessmentIdAndUniverse(ServerRequest request) {
        String id = request.pathVariable("id");
        String universe = request.pathVariable("universe");
        return this.service.getPersonageByPersonalityAssessmentIdAndUniverse(id,universe)
                .collectList()
                .flatMap(personageDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }
}
