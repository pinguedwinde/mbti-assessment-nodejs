package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.handler.AuthHandler;
import fr.lunatech.mbtiassessment.handler.UserHandler;
import fr.lunatech.mbtiassessment.model.User;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.USER_BASE_URL;
import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.EMAIL_ALREADY_EXISTS;
import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.USERNAME_ALREADY_EXISTS;
import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.BEARER_KEY;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class UserRouter {

    private final UserHandler handler;

    public UserRouter(UserHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "UserRouterFunction")
    @RouterOperations({
            @RouterOperation(
                    path = USER_BASE_URL + "/all", method = GET, beanClass = UserHandler.class, beanMethod = "getAllUsers",
                    produces = APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getAllUsers", summary = "Get all the users",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "All the users found",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/find/{username}", method = GET, beanClass = UserHandler.class, beanMethod = "findUser",
                    produces = APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "findUser", summary = "Find a user by using either a username or an email",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "username", schema = @Schema(type = "string")),
                                    @Parameter(in = ParameterIn.PATH, name = "email", schema = @Schema(type = "string"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The requested user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/profile-image/{username}", method = PUT, beanClass = UserHandler.class, beanMethod = "updateUserProfileImage",
                    consumes = MULTIPART_FORM_DATA_VALUE,
                    produces = APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "updateUserProfileImage", summary = "Update the user (with the given username) profile image with the given image",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "username", schema = @Schema(type = "string")),
                            },
                            requestBody = @RequestBody(
                                    required = true, description = "Give the user profile image to be updated",
                                    content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE,
                                            schema = @Schema(format = "binary", type = "file", requiredProperties = {"profileImage"}))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The requested user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/profile-image/{username}", method = GET, beanClass = UserHandler.class, beanMethod = "getTemporaryProfileImage",
                    produces = IMAGE_JPEG_VALUE,
                    operation = @Operation(
                            operationId = "getTemporaryProfileImage", summary = "Get the user temp profile image with the given username",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "username", schema = @Schema(type = "string")),
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The requested user",
                                            content = @Content()
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/image/{filename}", method = GET, beanClass = UserHandler.class, beanMethod = "getProfileImage",
                    produces = IMAGE_JPEG_VALUE,
                    operation = @Operation(
                            operationId = "getProfileImage", summary = "Get the user (with the given username) profile image with the given filename",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "filename", schema = @Schema(type = "string"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The requested user",
                                            content = @Content()
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/{id}", method = DELETE, beanClass = UserHandler.class, beanMethod = "deleteUser",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "deleteUser", summary = "Delete a user by using the id",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(type = "string"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The requested user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    )
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/add", method = POST, beanClass = UserHandler.class, beanMethod = "addUser",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "addUser", summary = "Add new user != Register a user",
                            requestBody = @RequestBody(
                                    required = true, description = "Give the user details",
                                    content = @Content(schema = @Schema(implementation = UserForm.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The created user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = USERNAME_ALREADY_EXISTS),
                                    @ApiResponse(responseCode = "400", description = EMAIL_ALREADY_EXISTS)
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = USER_BASE_URL + "/update", method = PUT, beanClass = AuthHandler.class, beanMethod = "updateUser",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "updateUser", summary = "Update user profile",
                            requestBody = @RequestBody(
                                    required = true, description = "Give the user update details",
                                    content = @Content(schema = @Schema(implementation = UserForm.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "The updated user",
                                            content = @Content(schema = @Schema(implementation = User.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = USERNAME_ALREADY_EXISTS),
                                    @ApiResponse(responseCode = "400", description = EMAIL_ALREADY_EXISTS),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction() {
        return route(GET(USER_BASE_URL).and(accept(APPLICATION_JSON)), handler::getAllUsers)
                .andRoute(PUT(USER_BASE_URL + "/update").and(contentType(APPLICATION_JSON)), handler::updateUser)
                .andRoute(GET(USER_BASE_URL + "/find/{username}").and(accept(APPLICATION_JSON)), handler::findUser)
                .andRoute(PUT(USER_BASE_URL + "/profile-image/{username}").and(contentType(MULTIPART_FORM_DATA)), handler::updateUserProfileImage)
                .andRoute(GET(USER_BASE_URL + "/profile-image/{username}").and(accept(IMAGE_JPEG)), handler::getTemporaryProfileImage)
                .andRoute(GET(USER_BASE_URL + "/image/{filename}").and(accept(IMAGE_JPEG)), handler::getProfileImage)
                .andRoute(POST(USER_BASE_URL + "/add").and(contentType(APPLICATION_JSON)), handler::addUser)
                .andRoute(DELETE(USER_BASE_URL + "/{id}").and(contentType(APPLICATION_JSON)), handler::deleteUser);
    }
}
