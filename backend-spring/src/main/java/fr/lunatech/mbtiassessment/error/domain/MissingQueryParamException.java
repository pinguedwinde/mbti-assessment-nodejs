package fr.lunatech.mbtiassessment.error.domain;

public class MissingQueryParamException extends RuntimeException {
    public MissingQueryParamException(String message) {
        super(message);
    }
}
