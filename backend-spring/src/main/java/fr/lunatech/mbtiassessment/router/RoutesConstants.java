package fr.lunatech.mbtiassessment.router;

public class RoutesConstants {
    public static final String AUTH_BASE_URL = "/auth";
    public static final String USER_BASE_URL = "/api/users";
    public static final String QUESTION_BASE_URL = "/api/questions";
    public static final String PERSONAGE_BASE_URL = "/api/personages";
    public static final String PERSONALITY_INFO_BASE_URL = "/api/personality-infos";
    public static final String PERSONALITY_ASSESSMENT_BASE_URL = "/api/personality-test";
    //public static final String[] PUBLIC_URLS = {"/**"};
    public static final String[] PUBLIC_URLS = {
            "/auth/login", "/auth/register", "/api/users/reset-password/**", "/api/users/profile-image/**",
            "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/api-docs/**"
    };
}
