package gs.controller.resource;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Error message to be returned on failure.
 */
@Data
@NoArgsConstructor
public class ErrorResource {

    private final List<String> messages = new ArrayList<>();

    public ErrorResource(String ... messages) {
        this(Arrays.asList(messages));
    }

    public ErrorResource(List<String> messages) {
        this.messages.addAll(messages);
    }
}
