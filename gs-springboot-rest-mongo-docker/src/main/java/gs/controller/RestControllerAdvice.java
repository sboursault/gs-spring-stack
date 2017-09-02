package gs.controller;

import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class RestControllerAdvice {


    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    VndErrors handleNotReadable(HttpMessageNotReadableException e) {
        // redefine the behaviors defined in DefaultHandlerExceptionResolver
        // to add the exception message in the response body
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler({InmateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    VndErrors handleNotFound(Exception e) {
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler({InvalidDataException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody VndErrors handleUnprocessable(InvalidDataException e) {
        return new VndErrors(e.getErrors().getAllErrors().stream()
                .map(error -> new VndErrors.VndError("error", error.getDefaultMessage()))
                .collect(toList()));
    }
}
