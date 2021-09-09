package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.AnswerDTO;
import fr.lunatech.mbtiassessment.dto.PersonalityAssessmentDTO;
import fr.lunatech.mbtiassessment.handler.PersonalityAssessmentHandler;
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

import static fr.lunatech.mbtiassessment.router.RoutesConstants.PERSONALITY_ASSESSMENT_BASE_URL;
import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.BEARER_KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class PersonalityAssessmentRouter {
    private final PersonalityAssessmentHandler handler;

    public PersonalityAssessmentRouter(PersonalityAssessmentHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "PersonalityAssessmentRouterFunction")
    @RouterOperations({
            @RouterOperation(
                    path = PERSONALITY_ASSESSMENT_BASE_URL, method = GET, beanClass = PersonalityAssessmentHandler.class,
                    beanMethod = "getAllPersonalityAssessments",
                    operation = @Operation(
                            operationId = "findAllPersonalityAssessments", summary = "Find all PersonalityAssessments without parameters",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonalityAssessmentDTO.class)))
                                    ),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_ASSESSMENT_BASE_URL + "/{username}", method = POST, beanClass = PersonalityAssessmentHandler.class,
                    beanMethod = "processPersonalityAssessment",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "processPersonalityAssessment", summary = "Process a personality assessment (test)",
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of an AnswerDTOs arrays",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnswerDTO.class)))
                            ),
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "username", description = "PersonalityAssessmentDTO username",
                                            schema = @Schema(description = "username", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Assessment processed",
                                            content = @Content(schema = @Schema(implementation = PersonalityAssessmentDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Problem with the questionnaire submitted"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_ASSESSMENT_BASE_URL + "/{username}", method = GET, beanClass = PersonalityAssessmentHandler.class,
                    beanMethod = "getPersonalityAssessmentByUsername",
                    operation = @Operation(
                            operationId = "getPersonalityAssessmentByUsername", summary = "Get a PersonalityAssessment by giving an Username",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "username", description = "PersonalityAssessmentDTO username",
                                            schema = @Schema(description = "username", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityAssessment not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_ASSESSMENT_BASE_URL + "/{id}", method = GET, beanClass = PersonalityAssessmentHandler.class,
                    beanMethod = "getPersonalityAssessmentById",
                    operation = @Operation(
                            operationId = "getPersonalityAssessmentById", summary = "Get a PersonalityAssessment by giving an Id",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "PersonalityAssessmentDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityAssessment not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = PERSONALITY_ASSESSMENT_BASE_URL +"all/{personalityAssessmentId}/{universe}", method = GET, beanClass = PersonalityAssessmentHandler.class,
                    beanMethod = "getPersonageByPersonalityAssessmentIdAndUniverse",
                    operation = @Operation(
                            operationId = "getPersonageByPersonalityAssessmentIdAndUniverse", summary = "Get a PersonalityAssessment by giving an Id",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "personalityAssessmentId", description = "PersonalityAssessmentDTO Id",
                                            schema = @Schema(description = "PersonalityAssessmentId",type = "string")
                                    ),
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "universe", description = "Personage universe",
                                            schema = @Schema(description = "Universe",type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "PersonalityAssessment not found"),
                            },
                            security = { @SecurityRequirement(name = BEARER_KEY)}
                    )
            )
    })
    public RouterFunction<ServerResponse> personalityAssessmentRouterFunction() {

        return route(GET(PERSONALITY_ASSESSMENT_BASE_URL).and(accept(APPLICATION_JSON)), handler::getAllPersonalityAssessments)
                .andRoute(POST(PERSONALITY_ASSESSMENT_BASE_URL + "/{username}").and(accept(APPLICATION_JSON)), handler::processPersonalityAssessment)
                .andRoute(GET(PERSONALITY_ASSESSMENT_BASE_URL + "/{username}").and(accept(APPLICATION_JSON)), handler::getPersonalityAssessmentByUsername)      
                .andRoute(GET(PERSONALITY_ASSESSMENT_BASE_URL + "/{id}").and(accept(APPLICATION_JSON)), handler::getPersonalityAssessmentById)
                .andRoute(GET(PERSONALITY_ASSESSMENT_BASE_URL + "all/{personalityAssessmentId}/{universe}").and(accept(APPLICATION_JSON)), handler::getPersonageByPersonalityAssessmentIdAndUniverse);
    }
}
