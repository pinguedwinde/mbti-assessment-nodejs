package fr.lunatech.mbtiassessment.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.*;
import static fr.lunatech.mbtiassessment.security.domain.SecurityConstants.BEARER_KEY;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-version}") String appVersion,
                                 @Value("${application-description}") String appDescription) {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_KEY,
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                )
                )
                .info(
                        new Info().title("Lunatech MBTI Assessment API")
                                .description(appDescription)
                                .version(appVersion)
                                .contact(new Contact().name("Benoit Montuelle").email("benoit.montuelle@lunatech.fr"))
                                .contact(new Contact().name("SILGA P. Fabrice").email("fabrice.silga@lunatech.fr"))
                                .termsOfService("https://swagger.io/terms/")
                                .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                );
    }

    @Bean
    public GroupedOpenApi mbtiAssessmentOpenApi() {
        String[] paths = {"/**"};
        return GroupedOpenApi.builder().group("MBTI Assessment").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi questionsOpenApi() {
        String[] paths = {QUESTION_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("Questions").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi personalityInfoOpenApi() {
        String[] paths = {PERSONALITY_INFO_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("PersonalityInfos").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi personagesOpenApi() {
        String[] paths = {PERSONAGE_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("Personages").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi personalityAssessmentOpenApi() {
        String[] paths = {PERSONALITY_ASSESSMENT_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("Personality-Assessment").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi userOpenApi() {
        String[] paths = {USER_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("Users").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi authOpenApi() {
        String[] paths = {AUTH_BASE_URL + "/**"};
        return GroupedOpenApi.builder().group("Auth").pathsToMatch(paths)
                .build();
    }

}
