package fr.lunatech.mbtiassessment.security.filter;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.TOKEN_JWT_HEADER;
import static org.springframework.http.HttpHeaders.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsWebFilter implements WebFilter {
    @Value("${allowed-origins}")
    public String allowed_origins;

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange serverWebExchange, @NonNull WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        List<String> ALLOWED_HEADERS = Arrays.asList(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE,
                ACCEPT, TOKEN_JWT_HEADER, AUTHORIZATION, "X-Requested-With",
                ACCESS_CONTROL_ALLOW_HEADERS, ACCESS_CONTROL_ALLOW_METHODS, ACCESS_CONTROL_ALLOW_CREDENTIALS);
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = serverWebExchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            headers.addAll(ACCESS_CONTROL_ALLOW_ORIGIN, Arrays.asList(allowed_origins)
            );
            headers.addAll(ACCESS_CONTROL_ALLOW_METHODS, Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            headers.add(ACCESS_CONTROL_MAX_AGE, "3600");
            headers.addAll(ACCESS_CONTROL_ALLOW_HEADERS, ALLOWED_HEADERS);
            headers.addAll(ACCESS_CONTROL_EXPOSE_HEADERS, ALLOWED_HEADERS);
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return webFilterChain.filter(serverWebExchange);
    }
}
