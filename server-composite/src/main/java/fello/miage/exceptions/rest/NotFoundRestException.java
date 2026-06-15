package fello.miage.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundRestException extends RuntimeException {
    public NotFoundRestException(String message) {
        super(message);
    }
}
