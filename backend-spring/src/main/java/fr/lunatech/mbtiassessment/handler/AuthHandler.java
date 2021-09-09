package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.security.domain.UserCredentials;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import fr.lunatech.mbtiassessment.security.domain.UserPrincipal;
import fr.lunatech.mbtiassessment.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.TOKEN_JWT_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Tag(name = "Authentication", description = "Endpoints for operations on Authentication")
public class AuthHandler {
    private final AuthService authService;
    private final RequestHandler requestHandler;

    public AuthHandler(AuthService authService, RequestHandler requestHandler) {
        this.authService = authService;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> registerUser(ServerRequest request) {
        return requestHandler.requireValidBodyMono(body -> body
                .flatMap(userForm -> this.authService.register(userForm, request))
                .flatMap(registeredUser -> ServerResponse.status(CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(registeredUser)
                ), request, UserForm.class);

    }

    public @NonNull Mono<ServerResponse> loginUser(ServerRequest request) {
        return requestHandler.requireValidBodyMono(body -> body
                .flatMap(this.authService::loginUser)
                .flatMap(loggedIndUser -> {
                    String jwtToken = this.authService.getToken(new UserPrincipal(loggedIndUser));
                    return ServerResponse.status(OK)
                            .contentType(APPLICATION_JSON)
                            .header(TOKEN_JWT_HEADER, jwtToken)
                            .bodyValue(loggedIndUser);
                }), request, UserCredentials.class);
    }
}
