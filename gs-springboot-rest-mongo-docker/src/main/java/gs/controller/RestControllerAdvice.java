package gs.controller;

import gs.controller.resource.ErrorResource;
import gs.exception.InmateNotFoundException;
import gs.exception.InvalidStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class RestControllerAdvice {

    // TODO: catch throwable

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResource handleNotReadable(HttpMessageNotReadableException e) {
        // redefine the behaviors defined in DefaultHandlerExceptionResolver
        // to add the exception message in the response body
        return new ErrorResource(e.getMessage());
    }

    @ExceptionHandler({InmateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResource handleNotFound(Exception e) {
        return new ErrorResource(e.getMessage());
    }

    @ExceptionHandler({InvalidStateException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody
    ErrorResource handleUnprocessable(InvalidStateException e) {
        return new ErrorResource(e.getErrors().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(toList()));
    }
}
