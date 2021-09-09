package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.handler.PersonalityInfoHandler;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
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

import static fr.lunatech.mbtiassessment.router.RoutesConstants.PERSONALITY_INFO_BASE_URL;
import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.BEARER_KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
public class PersonalityInfoRouter {

    private final PersonalityInfoHandler handler;

    public PersonalityInfoRouter(PersonalityInfoHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "PersonalityInfoRouterFunction")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY)})
    @RouterOperations({
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL, method = GET, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "getAllPersonalityInfos",
                    operation = @Operation(
                            operationId = "findAllPersonalityInfos", summary = "Find all personalityInfos without parameters",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonalityInfoDTO.class)))
                                    ),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL, method = POST, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "createPersonalityInfo",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "savePersonalityInfo", summary = "Save a personalityInfo",
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of PersonalityInfoDTO",
                                    content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "PersonalityInfo created",
                                            content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL, method = DELETE, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "deleteAllPersonalityInfos",
                    operation = @Operation(
                            operationId = "deleteAllPersonalityInfos", summary = "Delete all personalityInfos",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfos not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/personality-type/{personality-type}", method = GET, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "getPersonalityInfosByPersonalityType",
                    operation = @Operation(
                            operationId = "getPersonalityInfosByPersonalityType", summary = "Find all personalityInfos that match the given personalityType in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "personality-type", description = "PersonalityInfoDTO personalityType",
                                            schema = @Schema(description = "PersonalityType", type = "string", implementation = PersonalityType.class)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonalityInfoDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfos not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/id/{id}", method = GET, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "getPersonalityInfoById",
                    operation = @Operation(
                            operationId = "getPersonalityInfoById", summary = "Find the personalityInfo that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonalityInfoDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfo not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/profile/{profile}", method = GET, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "getPersonalityInfosByProfile",
                    operation = @Operation(
                            operationId = "getPersonalityInfosByProfile", summary = "Find the personalityInfo that matches the given profile in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "profile", description = "PersonalityInfoDTO Profile",
                                            schema = @Schema(description = "Profile", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfo not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/group/{group}", method = GET, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "getPersonalityInfosByGroup",
                    operation = @Operation(
                            operationId = "getPersonalityInfosByGroup", summary = "Find the personalityInfo that matches the given group in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "group", description = "PersonalityInfoDTO Group",
                                            schema = @Schema(description = "Group", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfo not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/{id}", method = PUT, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "updatePersonalityInfo",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "updatePersonalityInfo", summary = "Update a personalityInfo",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonalityInfoDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of PersonalityInfoDTO",
                                    content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonalityInfoDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfo not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_INFO_BASE_URL + "/{id}", method = DELETE, beanClass = PersonalityInfoHandler.class,
                    beanMethod = "deletePersonalityInfo",
                    operation = @Operation(
                            operationId = "deletePersonalityInfo", summary = "Delete the personalityInfo that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonalityInfoDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityInfo not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            )
    })
    public RouterFunction<ServerResponse> personalityInfosRouterFunction() {
        return route(GET(PERSONALITY_INFO_BASE_URL).and(accept(APPLICATION_JSON)), handler::getAllPersonalityInfos)
                .andRoute(POST(PERSONALITY_INFO_BASE_URL).and(contentType(APPLICATION_JSON)), handler::createPersonalityInfo)
                .andRoute(DELETE(PERSONALITY_INFO_BASE_URL).and(accept(APPLICATION_JSON)), handler::deleteAllPersonalityInfos)
                .andRoute(GET(PERSONALITY_INFO_BASE_URL + "/personality-type/{personality-type}").and(accept(APPLICATION_JSON)), handler::getPersonalityInfosByPersonalityType)
                .andRoute(GET(PERSONALITY_INFO_BASE_URL + "/id/{id}").and(accept(APPLICATION_JSON)), handler::getPersonalityInfoById)
                .andRoute(GET(PERSONALITY_INFO_BASE_URL + "/profile/{profile}").and(accept(APPLICATION_JSON)), handler::getPersonalityInfosByProfile)
                .andRoute(GET(PERSONALITY_INFO_BASE_URL + "/group/{group}").and(accept(APPLICATION_JSON)), handler::getPersonalityInfosByGroup)
                .andRoute(PUT(PERSONALITY_INFO_BASE_URL + "/{id}").and(contentType(APPLICATION_JSON)), handler::updatePersonalityInfo)
                .andRoute(DELETE(PERSONALITY_INFO_BASE_URL + "/{id}").and(accept(APPLICATION_JSON)), handler::deletePersonalityInfo);

    }
}
