package hu.baumannr.personregistryapi.exception;

import hu.baumannr.personregistryapi.mapper.PersonApiExceptionMapper;
import hu.baumannr.personregistryapi.rest.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

import static hu.baumannr.personregistryapi.constant.ErrorMessages.UNEXPECTED_ERROR;

/**
 * Global exception handler.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class PersonRegistryApiExceptionHandler {

    private final PersonApiExceptionMapper personApiExceptionMapper;

    @ExceptionHandler(PersonApiException.class)
    public ResponseEntity<ErrorResponse> handlePersonApiException(PersonApiException exception) {
        log.warn("API error: {}", exception.getMessage());
        ErrorResponse response = personApiExceptionMapper.convert(exception);
        return ResponseEntity.status(exception.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        String requestURL = exception.getRequestURL();
        String httpMethod = exception.getHttpMethod();
        String path = String.join(" ", httpMethod, requestURL);

        log.debug("No handler found for path: {}", path);

        ErrorResponse response = new ErrorResponse()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("No endpoint available for: " + path);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        log.debug(exception.getMessage());

        ErrorResponse response = new ErrorResponse()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .message(exception.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.error("Unexpected error", exception);

        ErrorResponse response = new ErrorResponse()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(UNEXPECTED_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
