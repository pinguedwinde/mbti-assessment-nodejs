package fr.lunatech.mbtiassessment.security.domain;

public class Authority {

    public static final String USER_READ = "user:read";
    public static final String USER_CREATE = "user:create";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";

    public static final String QUESTION_READ = "question:read";
    public static final String QUESTIONNAIRE_READ = "questionnaire:read";
    public static final String QUESTION_CREATE = "question:create";
    public static final String QUESTION_UPDATE = "question:update";
    public static final String QUESTION_DELETE = "question:delete";

    public static final String PERSONAGE_READ = "personage:read";
    public static final String PERSONAGE_CREATE = "personage:create";
    public static final String PERSONAGE_UPDATE = "personage:update";
    public static final String PERSONAGE_DELETE = "personage:delete";

    public static final String PERSONALITY_INFO_READ = "personality-info:read";
    public static final String PERSONALITY_INFO_CREATE = "personality-info:create";
    public static final String PERSONALITY_INFO_UPDATE = "personality-info:update";
    public static final String PERSONALITY_INFO_DELETE = "personality-info:delete";

    public static final String ASSESSMENT_READ = "assessment:read";
    public static final String ASSESSMENT_DELETE = "assessment:read";

    public static final String ANY_DELETE = "any:delete";

    public static final String DOCUMENTATION_READ = "documentation:read";

    public static final String[] USER_AUTHORITIES = {
            USER_READ, USER_UPDATE, QUESTIONNAIRE_READ, ASSESSMENT_READ, PERSONAGE_READ,
            PERSONALITY_INFO_READ
    };
    public static final String[] ADMIN_AUTHORITIES = {
            USER_READ, USER_CREATE, USER_UPDATE,
            PERSONAGE_READ, PERSONAGE_CREATE, PERSONAGE_UPDATE,
            QUESTION_READ, QUESTION_CREATE, QUESTION_UPDATE, ASSESSMENT_READ,
            PERSONALITY_INFO_READ, PERSONALITY_INFO_CREATE, PERSONALITY_INFO_UPDATE, PERSONALITY_INFO_UPDATE,
    };
    public static final String[] SUPER_ADMIN_AUTHORITIES = {
            USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
            QUESTION_READ, QUESTION_CREATE, QUESTION_UPDATE, QUESTION_DELETE,
            PERSONAGE_READ, PERSONAGE_CREATE, PERSONAGE_UPDATE, PERSONAGE_DELETE,
            PERSONALITY_INFO_READ, PERSONALITY_INFO_CREATE, PERSONALITY_INFO_UPDATE, PERSONALITY_INFO_DELETE,
            ASSESSMENT_DELETE, DOCUMENTATION_READ, ANY_DELETE
    };
}
