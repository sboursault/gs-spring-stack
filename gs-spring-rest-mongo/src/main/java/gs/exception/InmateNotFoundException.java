package gs.exception;

import gs.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InmateNotFoundException extends Exception {
    public InmateNotFoundException(String id) {
        super("no inmate found with id " + id);
    }
}
