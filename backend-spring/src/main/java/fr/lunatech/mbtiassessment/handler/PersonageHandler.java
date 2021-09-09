package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.service.PersonageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.error.domain.ExceptionConstants.PERSONAGE_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Tag(name = "Personage", description = "Endpoints for operations on Personages")
public class PersonageHandler {

    private final static NotFoundException NOT_FOUND_EXCEPTION =
            new NotFoundException(PERSONAGE_NOT_FOUND);
    private final PersonageService service;
    private final RequestHandler requestHandler;

    public PersonageHandler(PersonageService service, RequestHandler requestHandler) {
        this.service = service;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> getAllPersonages(ServerRequest request) {
        return this.service.getAllPersonages()
                .collectList()
                .flatMap(personageDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonageById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getPersonageById(id)
                .flatMap(personageDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonagesByPersonalityType(ServerRequest request) {
        String personalityType = request.pathVariable("personality-type");
        return this.service.getPersonagesByPersonalityType(personalityType)
                .collectList()
                .flatMap(personageDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getAllUniverse(ServerRequest request) {
        return this.service.getAllUniverse()
                .collectList()
                .flatMap(universeDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(universeDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonagesByUniverse(ServerRequest request) {
        String universe = request.pathVariable("universe");
        return this.service.getPersonagesByUniverse(universe)
                .collectList()
                .flatMap(personageDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonageByTypeAndUniverse(ServerRequest request) {
        String personalityType = request.pathVariable("personalityType");
        String universe = request.pathVariable("universe");
        return this.service.getPersonageByTypeAndUniverse(personalityType, universe)
                .collectList()
                .flatMap(personageDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> createPersonage(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body.flatMap(personageDTO -> {
            personageDTO.setId(null);
            return this.service.addPersonage(personageDTO);
        })
                .flatMap(personageDTO -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, PersonageDTO.class);

    }

    public @NonNull Mono<ServerResponse> updatePersonage(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(this.service::updatePersonage)
                .flatMap(personageDTO -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personageDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, PersonageDTO.class);

    }

    public @NonNull Mono<ServerResponse> deletePersonage(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getPersonageById(id)
                .flatMap(existingPersonage ->
                        ServerResponse.ok()
                                .build(this.service.deletePersonage(existingPersonage))
                )
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> deleteAllPersonages(ServerRequest request) {
        return ServerResponse.ok()
                .build(this.service.deleteAllPersonages());
    }
}
