package fr.lunatech.mbtiassessment.security.domain;

public class AuthConstants {
    public static final String TOKEN_EXPIRED_ERROR_MESSAGE = "The Jwt-Token is expired";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be not verified";
    public static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    public static final String ACCOUNT_DISABLED = "Your has been disabled. If this is an error, please contact administration";
    public static final String BAD_CREDENTIALS = "Username or Password incorrect. Please try again";
    public static final String UNAUTHORIZED_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have enough permissions to access";
    public static final String USERNAME_ALREADY_EXISTS = "This username is already taken";
    public static final String EMAIL_ALREADY_EXISTS = "This email address is already taken";
    public static final String USER_NOT_FOUND = "This user was not found";
    public static final String FOUND_USER_BY_USERNAME = "Returning found user by username : ";
    public static final String NO_USER_FOUND_BY_USERNAME = "No user found by username : ";
    public static final String NO_USER_FOUND_BY_EMAIL = "No user found by email : ";
    public static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
}
