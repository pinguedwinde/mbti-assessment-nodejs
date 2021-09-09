package fr.lunatech.mbtiassessment.security.domain;

import java.time.Duration;

public class SecurityConstants {
    public static final long TOKEN_EXPIRATION_TIME = Duration.ofDays(5).toMillis();
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_JWT_HEADER = "Jwt-Token";
    public static final String TOKEN_AUDIENCE = "Lunatech MBTI Assessment";
    public static final String TOKEN_ISSUER = "Lunatech MBTI Assessment, FR";
    public static final String AUTHORITIES = "authorities";
    public static final String BEARER_KEY = "bearer-key";
}
