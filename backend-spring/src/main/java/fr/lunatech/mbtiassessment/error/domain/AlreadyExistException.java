package fr.lunatech.mbtiassessment.error.domain;

public class AlreadyExistException extends Exception {
    public AlreadyExistException(String message) {
        super(message);
    }
}
