package co.edu.uniajc.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentRequestException extends RuntimeException {

    public StudentRequestException(String message) {
        super(message);
    }

    public StudentRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
