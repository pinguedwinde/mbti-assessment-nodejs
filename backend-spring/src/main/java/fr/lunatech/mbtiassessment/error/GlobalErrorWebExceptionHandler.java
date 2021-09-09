package fr.lunatech.mbtiassessment.error;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(GlobalErrorAttributes globalErrorAttributes, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private @NonNull Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        HttpStatus status = (HttpStatus) errorPropertiesMap.get("status");
        if (HttpStatus.BAD_REQUEST.equals(status)) {
            return getServerResponse(BAD_REQUEST, errorPropertiesMap);
        } else if (UNAUTHORIZED.equals(status)) {
            return getServerResponse(UNAUTHORIZED, errorPropertiesMap);
        } else if (FORBIDDEN.equals(status)) {
            return getServerResponse(FORBIDDEN, errorPropertiesMap);
        } else if (NOT_FOUND.equals(status)) {
            return getServerResponse(NOT_FOUND, errorPropertiesMap);
        } else if (METHOD_NOT_ALLOWED.equals(status)) {
            return getServerResponse(METHOD_NOT_ALLOWED, errorPropertiesMap);
        } else {
            return getServerResponse(INTERNAL_SERVER_ERROR, errorPropertiesMap);
        }
    }

    private Mono<ServerResponse> getServerResponse(HttpStatus httpStatus, Map<String, Object> errorPropertiesMap) {
        return ServerResponse.status(httpStatus)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
