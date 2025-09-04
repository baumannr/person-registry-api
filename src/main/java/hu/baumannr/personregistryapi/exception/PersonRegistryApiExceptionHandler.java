package hu.baumannr.personregistryapi.exception;

import hu.baumannr.personregistryapi.mapper.PersonApiExceptionMapper;
import hu.baumannr.personregistryapi.rest.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.error("Unexpected error", exception);
        ErrorResponse response = new ErrorResponse()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(UNEXPECTED_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
