package gs;

import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({InmateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    VndErrors handleNotFound(Exception e) {
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler({InvalidDataException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody VndErrors handleUnprocessable(InvalidDataException e) {
        return new VndErrors(e.getErrorMessages().stream()
                .map(msg -> new VndErrors.VndError("error", msg))
                .collect(toList()));
    }
}
