package gs.util;

import gs.exception.InvalidDataException;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by me on 27/07/17.
 */
public class RestPreconditions {

    public static void checkNotNull(Object input, String errorMessage, List<String> errors) {
        if (input == null) {
            errors.add(errorMessage);
        }
    }

    public static void checkNull(Object input, String errorMessage, List<String> errors) {
        if (input != null) {
            errors.add(errorMessage);
        }
    }

    public static void checkNotEmpty(String input, String errorMessage, List<String> errors) {
        if (StringUtils.isEmpty(input)) {
            errors.add(errorMessage);
        }
    }
}
