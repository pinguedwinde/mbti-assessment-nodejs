package fr.lunatech.mbtiassessment.error;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import fr.lunatech.mbtiassessment.error.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static fr.lunatech.mbtiassessment.error.domain.ErrorAttributesConstants.*;
import static fr.lunatech.mbtiassessment.error.domain.ExceptionConstants.*;
import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.*;
import static fr.lunatech.mbtiassessment.service.util.FileConstants.IMAGE_FILE_NOT_FOUND;
import static org.springframework.http.HttpStatus.*;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    private static final Log LOGGER = LogFactory.getLog(GlobalErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        errorAttributes.remove(ATTRIBUTE_REQUEST_ID);
        errorAttributes.remove(ATTRIBUTE_ERROR);
        errorAttributes.remove(ATTRIBUTE_TRACE);
        Throwable error = getError(request);
        LOGGER.error(error.getMessage());
        if (error instanceof UndefinedFactorException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, UNDEFINED_FACTOR_MESSAGE);
        } else if (error instanceof UndefinedPersonalityTypeException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, UNDEFINED_PERSONALITY_TYPE_MESSAGE);
        } else if (error instanceof NotFoundException) {
            setErrorAttributes(errorAttributes, NOT_FOUND, error.getMessage());
        } else if (error instanceof DisabledException) {
            setErrorAttributes(errorAttributes, FORBIDDEN, ACCOUNT_DISABLED);
        } else if (error instanceof LockedException) {
            setErrorAttributes(errorAttributes, FORBIDDEN, ACCOUNT_LOCKED);
        } else if (error instanceof BadCredentialsException || error instanceof UsernameNotFoundException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, BAD_CREDENTIALS);
        } else if (error instanceof AccessDeniedException) {
            setErrorAttributes(errorAttributes, FORBIDDEN, ACCESS_DENIED_MESSAGE);
        } else if (error instanceof AuthenticationException) {
            setErrorAttributes(errorAttributes, UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
        } else if (error instanceof AlreadyExistException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, error.getMessage());
        } else if (error instanceof TokenExpiredException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, TOKEN_EXPIRED_ERROR_MESSAGE);
        } else if (error instanceof MethodNotAllowedException) {
            HttpMethod supportedMethod = ((MethodNotAllowedException) error).getSupportedMethods().iterator().next();
            String message = String.format(METHOD_IS_NOT_ALLOWED, supportedMethod);
            setErrorAttributes(errorAttributes, METHOD_NOT_ALLOWED, message);
        } else if (error instanceof ServerWebInputException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, error.getMessage());
        } else if (error instanceof MissingQueryParamException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, error.getMessage());
        } else if (error instanceof ResponseStatusException) {
            setErrorAttributes(errorAttributes, NOT_FOUND, NO_RESOURCE_FOUND_MESSAGE);
        } else if (error instanceof SignatureVerificationException || error instanceof JWTDecodeException
                || error instanceof InvalidClaimException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, TOKEN_CANNOT_BE_VERIFIED);
        } else if (error instanceof NoSuchFileException) {
            setErrorAttributes(errorAttributes, NOT_FOUND, IMAGE_FILE_NOT_FOUND);
        } else if (error instanceof NotAnImageFileException) {
            setErrorAttributes(errorAttributes, BAD_REQUEST, error.getMessage());
        } else {
            setErrorAttributes(errorAttributes, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_MESSAGE);
        }
        return errorAttributes;
    }

    private void setErrorAttributes(Map<String, Object> errorAttributes, HttpStatus httpStatus, String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        errorAttributes.put(ATTRIBUTE_TIMESTAMP, simpleDateFormat.format(new Date()));
        errorAttributes.put(ATTRIBUTE_STATUS, httpStatus);
        errorAttributes.put(ATTRIBUTE_CODE, httpStatus.value());
        errorAttributes.put(ATTRIBUTE_REASON, httpStatus.getReasonPhrase());
        errorAttributes.put(ATTRIBUTE_MESSAGE, message);
    }
}
