package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.security.validator.ValidList;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fr.lunatech.mbtiassessment.model.util.CustomHttpResponse.renderCustomResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class RequestHandler {
    private final Validator validator;

    public RequestHandler(Validator validator) {
        this.validator = validator;
    }

    public <BODY> Mono<ServerResponse> requireValidBodyMono(
            Function<Mono<BODY>, Mono<ServerResponse>> block,
            ServerRequest request, Class<BODY> bodyClass) {
        return request
                .bodyToMono(bodyClass)
                .flatMap(
                        body -> {
                            Set<ConstraintViolation<BODY>> constraintViolations = validator.validate(body);
                            if (constraintViolations.isEmpty()) {
                                return block.apply(Mono.just(body));
                            } else {
                                List<String> messages = constraintViolations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .collect(Collectors.toList());
                                return ServerResponse.badRequest()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(renderCustomResponse(BAD_REQUEST, messages));
                            }
                        }
                );
    }

    public <BODY> Mono<ServerResponse> requireValidBodyFlux(
            Function<Flux<BODY>, Mono<ServerResponse>> block,
            ServerRequest request, Class<BODY> bodyClass) {
        return request
                .bodyToFlux(bodyClass)
                .collectList()
                .flatMap(
                        body -> {
                            ValidList<BODY> validList = new ValidList<>(body);
                            Set<ConstraintViolation<ValidList<BODY>>> constraintViolations = validator.validate(validList);
                            if (constraintViolations.isEmpty()) {
                                return block.apply(Flux.fromIterable(body));
                            } else {
                                List<String> messages = constraintViolations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .collect(Collectors.toList());
                                return ServerResponse.badRequest()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(renderCustomResponse(BAD_REQUEST, messages));
                            }
                        }
                );
    }
}
