package gs.exception;

import com.google.common.base.Joiner;
import org.springframework.util.Assert;

import java.util.List;

public class InvalidDataException extends Exception {

    List<String> errorMessages;

    public InvalidDataException(List<String> errorMessages) {
        super(Joiner.on(",").join(errorMessages));
        Assert.notEmpty(errorMessages, "error messages can't be empty");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
