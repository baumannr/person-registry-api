package hu.baumannr.personregistryapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Person API exception.
 */
@Getter
@RequiredArgsConstructor
public class PersonApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -103587968877846941L;

    private final LocalDateTime timestamp = LocalDateTime.now();

    private final HttpStatus statusCode;

    private final String message;

}
