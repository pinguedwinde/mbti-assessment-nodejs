package fr.lunatech.mbtiassessment.security.configuration;

import fr.lunatech.mbtiassessment.security.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.*;
import static fr.lunatech.mbtiassessment.security.domain.Authority.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Profile({"!test"})
public class WebfluxSecurityConfiguration {
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final BCryptPasswordEncoder passwordEncoder;

    public WebfluxSecurityConfiguration(ReactiveUserDetailsService reactiveUserDetailsService,
                                        JwtAuthorizationFilter jwtAuthorizationFilter,
                                        BCryptPasswordEncoder passwordEncoder) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(this.reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(this.passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors()
                .and()
                .authorizeExchange()
                .pathMatchers(PUBLIC_URLS).permitAll()
                .pathMatchers(OPTIONS).permitAll()
                .pathMatchers(GET, QUESTION_BASE_URL + "/questionnaire").hasAuthority(QUESTIONNAIRE_READ)
                .pathMatchers(GET, QUESTION_BASE_URL).hasAuthority(QUESTION_READ)
                .pathMatchers(GET, PERSONAGE_BASE_URL).hasAuthority(PERSONAGE_READ)
                .pathMatchers(GET, PERSONALITY_ASSESSMENT_BASE_URL).hasAuthority(ASSESSMENT_READ)
                .pathMatchers(GET, PERSONALITY_INFO_BASE_URL).hasAuthority(PERSONALITY_INFO_READ)
                .pathMatchers(GET, USER_BASE_URL).hasAuthority(USER_READ)
                .pathMatchers(POST, QUESTION_BASE_URL).hasAuthority(QUESTION_CREATE)
                .pathMatchers(POST, PERSONAGE_BASE_URL).hasAuthority(PERSONAGE_CREATE)
                .pathMatchers(POST, PERSONALITY_INFO_BASE_URL).hasAuthority(PERSONALITY_INFO_CREATE)
                .pathMatchers(POST, USER_BASE_URL).hasAuthority(USER_CREATE)
                .pathMatchers(PUT, QUESTION_BASE_URL).hasAuthority(QUESTION_UPDATE)
                .pathMatchers(PUT, PERSONAGE_BASE_URL).hasAuthority(PERSONAGE_UPDATE)
                .pathMatchers(PUT, PERSONALITY_INFO_BASE_URL).hasAuthority(PERSONALITY_INFO_UPDATE)
                .pathMatchers(PUT, USER_BASE_URL).hasAuthority(USER_UPDATE)
                .pathMatchers(DELETE).hasRole(ANY_DELETE)
                .anyExchange().authenticated()
                .and().securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling()
                .authenticationEntryPoint((exchange, exception) -> Mono.error(exception))
                .accessDeniedHandler((exchange, exception) -> Mono.error(exception))
                .and().addFilterAfter(this.jwtAuthorizationFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .formLogin().disable()
                .logout().disable()
                .csrf().disable()
                .httpBasic().disable()
                .build();
    }
}
