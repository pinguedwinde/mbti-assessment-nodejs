package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.handler.PersonageHandler;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import fr.lunatech.mbtiassessment.model.util.Universe;
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

import static fr.lunatech.mbtiassessment.router.RoutesConstants.PERSONAGE_BASE_URL;
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
public class PersonageRouter {

    private final PersonageHandler handler;

    public PersonageRouter(PersonageHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "PersonageRouterFunction")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY) })
    @RouterOperations({
            @RouterOperation(
                    path = PERSONAGE_BASE_URL, method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getAllPersonages",
                    operation = @Operation(
                            operationId = "findAllPersonages", summary = "Find all personages without parameters",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonageDTO.class)))
                                    ),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL, method = POST, beanClass = PersonageHandler.class,
                    beanMethod = "createPersonage",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "savePersonage", summary = "Save a personage",
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of PersonageDTO",
                                    content = @Content(schema = @Schema(implementation = PersonageDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Personage created",
                                            content = @Content(schema = @Schema(implementation = PersonageDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL, method = DELETE, beanClass = PersonageHandler.class,
                    beanMethod = "deleteAllPersonages",
                    operation = @Operation(
                            operationId = "deleteAllPersonages", summary = "Delete all personages",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Personages not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL+"/personality-type/{personality-type}", method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getPersonagesByPersonalityType",
                    operation = @Operation(
                            operationId = "getPersonagesByPersonalityType", summary = "Find all personages that match the given personalityType in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "ersonality-type}", description = "PersonageDTO personalityType",
                                            schema = @Schema(description = "PersonalityType", type = "string", implementation = PersonalityType.class)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonageDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                                    @ApiResponse(responseCode = "404", description = "Personages not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL + "/universe", method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getAllUniverse",
                    operation = @Operation(
                            operationId = "getAllUniverse", summary = "Get all Universe",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonageDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Bad request"),
                                    @ApiResponse(responseCode = "404", description = "Questions not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL + "/universe/{universe}", method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getPersonagesByUniverse",
                    operation = @Operation(
                            operationId = "getPersonagesByUniverse", summary = "Find all personages that match the given universe in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "universe", description = "PersonageDTO universe",
                                            schema = @Schema(description = "Universe", type = "string", implementation = Universe.class)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonageDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined universe supplied"),
                                    @ApiResponse(responseCode = "404", description = "Personages not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL + "/all/{personalityType}/{universe}", method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getPersonageByTypeAndUniverse",
                    operation = @Operation(
                            operationId = "getPersonageByTypeAndUniverse", summary = "Find all universe that match the given personalityType and universe in parameters",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "personalityType", description = "PersonageDTO personalityType",
                                            schema = @Schema(description = "PersonalityType", type = "string", implementation = PersonalityType.class)
                                    ),
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "universe", description = "PersonageDTO personalityType",
                                            schema = @Schema(description = "Universe", type = "string", implementation = PersonageDTO.class)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonageDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined universe supplied"),
                                    @ApiResponse(responseCode = "404", description = "Personages not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL+"/id/{id}", method = GET, beanClass = PersonageHandler.class,
                    beanMethod = "getPersonageById",
                    operation = @Operation(
                            operationId = "getPersonageById", summary = "Find the personage that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonageDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonageDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Personage not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL+"/{id}", method = PUT, beanClass = PersonageHandler.class,
                    beanMethod = "updatePersonage",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "updatePersonage", summary = "Update a personage",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonageDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of PersonageDTO",
                                    content = @Content(schema = @Schema(implementation = PersonageDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = PersonageDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined PersonalityType supplied"),
                                    @ApiResponse(responseCode = "404", description = "Personage not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONAGE_BASE_URL+"{id}", method = DELETE, beanClass = PersonageHandler.class,
                    beanMethod = "deletePersonage",
                    operation = @Operation(
                            operationId = "deletePersonage", summary = "Delete the personage that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonageDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Personage not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            )
    })
    public RouterFunction<ServerResponse> personagesRouterFunction() {

        return route(GET(PERSONAGE_BASE_URL).and(accept(APPLICATION_JSON)), handler::getAllPersonages)
                .andRoute(POST(PERSONAGE_BASE_URL).and(contentType(APPLICATION_JSON)), handler::createPersonage)
                .andRoute(DELETE(PERSONAGE_BASE_URL).and(accept(APPLICATION_JSON)), handler::deleteAllPersonages)
                .andRoute(GET(PERSONAGE_BASE_URL + "/personality-type/{personality-type}").and(accept(APPLICATION_JSON)), handler::getPersonagesByPersonalityType)
                .andRoute(GET(PERSONAGE_BASE_URL + "/universe").and(accept(APPLICATION_JSON)), handler::getAllUniverse)
                .andRoute(GET(PERSONAGE_BASE_URL + "/universe/{universe}").and(accept(APPLICATION_JSON)), handler::getPersonagesByUniverse)
                .andRoute(GET(PERSONAGE_BASE_URL + "/all/{personalityType}/{universe}").and(accept(APPLICATION_JSON)), handler::getPersonageByTypeAndUniverse)
                .andRoute(GET(PERSONAGE_BASE_URL + "/id/{id}").and(accept(APPLICATION_JSON)), handler::getPersonageById)
                .andRoute(PUT(PERSONAGE_BASE_URL + "/{id}").and(contentType(APPLICATION_JSON)), handler::updatePersonage)
                .andRoute(DELETE(PERSONAGE_BASE_URL + "/{id}").and(accept(APPLICATION_JSON)), handler::deletePersonage);

    }
}
