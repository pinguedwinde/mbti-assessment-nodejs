package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.service.PersonalityInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.error.domain.ExceptionConstants.PERSONALITY_INFO_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Tag(name = "PersonalityInfo", description = "Endpoints for operations on Personality Infos")
public class PersonalityInfoHandler {
    private final static NotFoundException NOT_FOUND_EXCEPTION =
            new NotFoundException(PERSONALITY_INFO_NOT_FOUND);
    private final PersonalityInfoService service;
    private final RequestHandler requestHandler;

    public PersonalityInfoHandler(PersonalityInfoService service, RequestHandler requestHandler) {
        this.service = service;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> getAllPersonalityInfos(ServerRequest request) {
        return this.service.getAllPersonalityInfos()
                .collectList()
                .flatMap(personalityInfoDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonalityInfoById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getPersonalityInfoById(id)
                .flatMap(personalityInfoDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(Mono.just(personalityInfoDTO)))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public Mono<ServerResponse> getPersonalityInfosByPersonalityType(ServerRequest request) {
        String personalityType = request.pathVariable("personality-type");
        return this.service.getPersonalityInfoByPersonalityType(personalityType)
                .flatMap(personalityInfoDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonalityInfosByProfile(ServerRequest request) {
        String profile = request.pathVariable("profile");
        return this.service.getPersonalityInfoByProfile(profile)
                .collectList()
                .flatMap(personalityInfoDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> getPersonalityInfosByGroup(ServerRequest request) {
        String group = request.pathVariable("group");
        return this.service.getPersonalityInfoByGroup(group)
                .collectList()
                .flatMap(personalityInfoDTOs -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTOs))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> createPersonalityInfo(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(personalityInfoDTO -> {
                    personalityInfoDTO.setId(null);
                    return this.service.addPersonalityInfo(personalityInfoDTO);
                })
                .flatMap(personalityInfoDTO -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, PersonalityInfoDTO.class);
    }

    public @NonNull Mono<ServerResponse> updatePersonalityInfo(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(this.service::updatePersonalityInfo)
                .flatMap(personalityInfoDTO -> ServerResponse.status(HttpStatus.OK)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(personalityInfoDTO))
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION)), request, PersonalityInfoDTO.class);
    }

    public @NonNull Mono<ServerResponse> deletePersonalityInfo(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.service.getPersonalityInfoById(id)
                .flatMap(existingPersonalityInfo ->
                        ServerResponse.ok()
                                .build(this.service.deletePersonalityInfo(existingPersonalityInfo))
                )
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> deleteAllPersonalityInfos(ServerRequest request) {
        return ServerResponse.ok()
                .build(this.service.deleteAllPersonalityInfos());
    }
}
