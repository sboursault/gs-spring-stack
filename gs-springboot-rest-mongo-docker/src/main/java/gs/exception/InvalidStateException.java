package gs.exception;

import com.google.common.base.Joiner;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidStateException extends Exception {

    Errors errors;

    public InvalidStateException(Errors errors) {
        super(errors.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        Assert.isTrue(errors.hasErrors(), "no error registered");
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
