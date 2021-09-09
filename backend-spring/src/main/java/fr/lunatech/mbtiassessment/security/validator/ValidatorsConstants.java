package fr.lunatech.mbtiassessment.security.validator;

public class ValidatorsConstants {
    public static final int NAME_MIN = 3;
    public static final int NAME_MAX = 60;
    public static final int UNAME_MIN = 5;
    public static final int PASSWORD_MAX = 20;
    public static final int PASSWORD_MIN = 8;
    public static final int EMAIL_MAX = 60;
    public static final int ANSWER_MIN = 1;
    public static final int ANSWER_MAX = 7;
    public static final int ID_LENGTH = 24;
    public static final int DESC_MAX = 1024;
    public static final String NOT_BLANK = " cannot be blank.";
    public static final String NOT_NULL = " cannot be null.";
    public static final String VALID_EMAIL = "Email should be a valid email.";
    public static final String NAME_SIZE = " size must be between " + NAME_MIN + " and " + NAME_MAX + ".";
    public static final String UNAME_SIZE = " size must be between " + UNAME_MIN + " and " + NAME_MAX + ".";
    public static final String PASSWORD_SIZE = " size must be between " + PASSWORD_MIN + " and " + PASSWORD_MAX + ".";
    public static final String EMAIL_SIZE = "Email max length requirement is " + EMAIL_MAX + ".";
    public static final String ID_SIZE = "ID length requirement is " + ID_LENGTH;
    public static final String MIN_VALUE = " min value requirement is ";
    public static final String MAX_VALUE = " max value requirement is ";
    public static final String PERSONALITY_TYPE_PATTERN = "Please give a valid PersonalityType as XXXX_X.";
}
