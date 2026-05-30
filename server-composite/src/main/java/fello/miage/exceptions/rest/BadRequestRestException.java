package fello.miage.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestRestException extends RuntimeException {
    public BadRequestRestException(String message) {
        super(message);
    }
}
