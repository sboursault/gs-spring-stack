package gs.util;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Created by me on 27/07/17.
 */
public class RestPreconditions {

    public static void checkNotNull(String value, String field, Errors errors) {
        if (value == null) {
            errors.reject(field + ".null", field + " must not be null");
        }
    }

    public static void checkNull(String value, String field, Errors errors) {
        if (value != null) {
            errors.reject(field + ".not-null", field + " must be null");
        }
    }

    public static void checkNotEmpty(String value, String field, Errors errors) {
        if (StringUtils.isEmpty(value)) {
            errors.reject(field + ".empty", field + " must not be null or empty");
        }
    }

    public static void check(boolean test, String field, String errorMessage, Errors errors) {
        if (!test) {
            errors.reject(field + ".error", errorMessage);
        }
    }
}
