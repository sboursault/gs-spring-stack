package gs.util;

import gs.exception.InvalidStateException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Created by sboursault on 27/07/17.
 */
public class ValidationUtils {

    public static void validateNotNull(String value, String field, Errors errors) throws InvalidStateException {
        if (!checkNotNull(value, field, errors)) {
            throw new InvalidStateException(errors);
        }
    }

    public static boolean checkNotNull(String value, String field, Errors errors) {
        if (value == null) {
            errors.reject(field + ".null", field + " must not be null");
            return false;
        }
        return true;
    }

    public static void validateNull(String value, String field, Errors errors) throws InvalidStateException {
        if (!checkNull(value, field, errors)) {
            throw new InvalidStateException(errors);
        }
    }

    public static boolean checkNull(String value, String field, Errors errors) {
        if (value != null) {
            errors.reject(field + ".not-null", field + " must be null");
            return false;
        }
        return true;
    }

    public static void validateNotEmpty(String value, String field, Errors errors) throws InvalidStateException {
        if (!checkNotEmpty(value, field, errors)) {
            throw new InvalidStateException(errors);
        }
    }

    public static boolean checkNotEmpty(String value, String field, Errors errors) {
        if (StringUtils.isEmpty(value)) {
            errors.reject(field + ".empty", field + " must not be null or empty");
            return false;
        }
        return true;
    }

    public static void validate(boolean test, String field, String errorMessage, Errors errors) throws InvalidStateException {
        if (!check(test, field, errorMessage, errors)) {
            throw new InvalidStateException(errors);
        }
    }

    public static boolean check(boolean test, String field, String errorMessage, Errors errors) {
        if (!test) {
            errors.reject(field + ".error", errorMessage);
            return false;
        }
        return true;
    }
}
