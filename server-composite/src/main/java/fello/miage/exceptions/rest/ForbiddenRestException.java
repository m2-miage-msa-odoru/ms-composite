package fello.miage.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenRestException extends RuntimeException {
    public ForbiddenRestException(String message) {
        super(message);
    }
}
