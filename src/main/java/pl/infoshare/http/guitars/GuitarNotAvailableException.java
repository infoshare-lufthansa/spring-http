package pl.infoshare.http.guitars;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GuitarNotAvailableException extends RuntimeException {

    public GuitarNotAvailableException(String id) {
        super(String.format("Guitar with id %s is out of stock!", id));
    }
}
