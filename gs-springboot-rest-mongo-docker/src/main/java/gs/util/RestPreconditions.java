package gs.util;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Created by me on 27/07/17.
 */
public class RestPreconditions {

    public static void checkNotNull(String field, Object value, String errorMessage, Errors errors) {
        if (value == null) {
            errors.reject(field + ".null", errorMessage);
        }
    }

    public static void checkNull(String field, Object value, String errorMessage, Errors errors) {
        if (value != null) {
            errors.reject(field + ".not-null", errorMessage);
        }
    }

    public static void checkNotEmpty(String field, String value, String errorMessage, Errors errors) {
        if (StringUtils.isEmpty(value)) {
            errors.reject(field + ".empty", errorMessage);
        }
    }
}
