package fr.lunatech.mbtiassessment.security.filter;

import fr.lunatech.mbtiassessment.security.jwt.JwtTokenProvider;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.TOKEN_PREFIX;

@Component
public class JwtAuthorizationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange serverWebExchange, @NonNull WebFilterChain filterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        } else {
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if ((Objects.nonNull(authorizationHeader) && !authorizationHeader.startsWith(TOKEN_PREFIX))
                    || Objects.isNull(authorizationHeader)) {
                return filterChain.filter(serverWebExchange);
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length()).trim();
            String username = this.jwtTokenProvider.getSubjectFromToken(token);
            if (this.jwtTokenProvider.isTokenValid(username, token)) {
                List<GrantedAuthority> authorities = this.jwtTokenProvider.getAuthoritiesFromToken(token);
                Authentication authentication = this.jwtTokenProvider.getAuthentication(username, authorities);
                return filterChain.filter(serverWebExchange)
                        .contextWrite(context -> ReactiveSecurityContextHolder.withAuthentication(authentication));

            }
        }
        return filterChain.filter(serverWebExchange);
    }
}
