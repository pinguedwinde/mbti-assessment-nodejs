package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.handler.AuthHandler;
import fr.lunatech.mbtiassessment.model.User;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.AUTH_BASE_URL;
import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@EnableWebFlux
public class AuthRouter {

    private final AuthHandler handler;

    public AuthRouter(AuthHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "AuthRouterFunction")
    @RouterOperations({
            @RouterOperation(
                    path = AUTH_BASE_URL + "/login", method = POST, beanClass = AuthHandler.class, beanMethod = "loginUser",
                    produces = APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "login", summary = "The login endpoint",
                            requestBody = @RequestBody(
                                    required = true, description = "Give the login details : username and password only are mandatory",
                                    content = @Content(schema = @Schema(implementation = UserForm.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The loggedIn user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = BAD_CREDENTIALS),
                            }
                    )
            ),
            @RouterOperation(
                    path = AUTH_BASE_URL + "/register", method = POST, beanClass = AuthHandler.class, beanMethod = "registerUser",
                    produces = APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "register", summary = "The registration endpoint",
                            requestBody = @RequestBody(
                                    required = true, description = "Give the registration details",
                                    content = @Content(schema = @Schema(implementation = UserForm.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The registered user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = USERNAME_ALREADY_EXISTS),
                                    @ApiResponse(responseCode = "400", description = EMAIL_ALREADY_EXISTS),
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> authRouterFunction() {
        return route(POST(AUTH_BASE_URL + "/register").and(contentType(APPLICATION_JSON)), handler::registerUser)
                .andRoute(POST(AUTH_BASE_URL + "/login").and(contentType(APPLICATION_JSON)), handler::loginUser);

    }
}
