package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.handler.QuestionHandler;
import fr.lunatech.mbtiassessment.model.util.Factor;
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

import static fr.lunatech.mbtiassessment.router.RoutesConstants.QUESTION_BASE_URL;
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
public class QuestionRouter {

    private final QuestionHandler handler;

    public QuestionRouter(QuestionHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "QuestionRouterFunction")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY)})
    @RouterOperations({
            @RouterOperation(
                    path = QUESTION_BASE_URL, method = GET, beanClass = QuestionHandler.class,
                    beanMethod = "getAllQuestions",
                    operation = @Operation(
                            operationId = "findAllQuestions", summary = "Find all questions without parameters",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDTO.class)))
                                    ),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL, method = POST, beanClass = QuestionHandler.class,
                    beanMethod = "createQuestion",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "saveQuestion", summary = "Save a question",
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of QuestionDTO",
                                    content = @Content(schema = @Schema(implementation = QuestionDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Question created",
                                            content = @Content(schema = @Schema(implementation = QuestionDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined Factor supplied"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL, method = DELETE, beanClass = QuestionHandler.class,
                    beanMethod = "deleteAllQuestions",
                    operation = @Operation(
                            operationId = "deleteAllQuestions", summary = "Delete all questions",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Questions not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL + "/questionnaire", method = GET, beanClass = QuestionHandler.class,
                    beanMethod = "getQuestionnaire",
                    operation = @Operation(
                            operationId = "getQuestionnaire", summary = "Get questions of a question for an assessment",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Bad request"),
                                    @ApiResponse(responseCode = "404", description = "Questions not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL + "/factor/{factor}", method = GET, beanClass = QuestionHandler.class,
                    beanMethod = "getQuestionsByFactor",
                    operation = @Operation(
                            operationId = "getQuestionsByFactor", summary = "Find all questions that match the given personalityType in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "factor", description = "QuestionDTO personalityType",
                                            schema = @Schema(description = "Factor", type = "string", implementation = Factor.class)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDTO.class)))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined Factor supplied"),
                                    @ApiResponse(responseCode = "404", description = "Questions not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL + "/id/{id}", method = GET, beanClass = QuestionHandler.class,
                    beanMethod = "getQuestionById",
                    operation = @Operation(
                            operationId = "getQuestionById", summary = "Find the question that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "QuestionDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = QuestionDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Question not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL + "/{id}", method = PUT, beanClass = QuestionHandler.class,
                    beanMethod = "updateQuestion",
                    produces = "application/json",
                    operation = @Operation(
                            operationId = "updateQuestion", summary = "Update a question",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "QuestionDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true, description = "Give a JSON of QuestionDTO",
                                    content = @Content(schema = @Schema(implementation = QuestionDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(schema = @Schema(implementation = QuestionDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Undefined Factor supplied"),
                                    @ApiResponse(responseCode = "404", description = "Question not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            ),
            @RouterOperation(
                    path = QUESTION_BASE_URL + "/{id}", method = DELETE, beanClass = QuestionHandler.class,
                    beanMethod = "deleteQuestion",
                    operation = @Operation(
                            operationId = "deleteQuestion", summary = "Delete the question that matches the given Id in parameter",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH, name = "id", description = "QuestionDTO Id",
                                            schema = @Schema(description = "Id", type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content()
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Question not found"),
                            },
                            security = {@SecurityRequirement(name = BEARER_KEY)}
                    )
            )
    })
    public RouterFunction<ServerResponse> questionnairesRouterFunction() {

        return route(GET(QUESTION_BASE_URL).and(accept(APPLICATION_JSON)), handler::getAllQuestions)
                .andRoute(POST(QUESTION_BASE_URL).and(contentType(APPLICATION_JSON)), handler::createQuestion)
                .andRoute(DELETE(QUESTION_BASE_URL).and(accept(APPLICATION_JSON)), handler::deleteAllQuestions)
                .andRoute(GET(QUESTION_BASE_URL + "/questionnaire").and(accept(APPLICATION_JSON)), handler::getQuestionnaire)
                .andRoute(GET(QUESTION_BASE_URL + "/factor/{factor}").and(accept(APPLICATION_JSON)), handler::getQuestionsByFactor)
                .andRoute(GET(QUESTION_BASE_URL + "/id/{id}").and(accept(APPLICATION_JSON)), handler::getQuestionById)
                .andRoute(PUT(QUESTION_BASE_URL + "/{id}").and(contentType(APPLICATION_JSON)), handler::updateQuestion)
                .andRoute(DELETE(QUESTION_BASE_URL + "/{id}").and(accept(APPLICATION_JSON)), handler::deleteQuestion);

    }


}
