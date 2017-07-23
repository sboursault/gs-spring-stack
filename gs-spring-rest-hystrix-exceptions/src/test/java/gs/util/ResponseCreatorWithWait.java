package gs.util;

import gs.DemoTests;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.web.client.response.DefaultResponseCreator;

import java.io.IOException;

/**
 * Created by me on 23/07/17.
 */
public class ResponseCreatorWithWait extends DefaultResponseCreator {

    private final int millis;

    protected ResponseCreatorWithWait(HttpStatus statusCode, int millis) {
        super(statusCode);
        this.millis = millis;
    }

    public static DefaultResponseCreator withSuccessAfter(int millis) {
        return new ResponseCreatorWithWait(HttpStatus.OK, millis);
    }

    @Override
    public ClientHttpResponse createResponse(ClientHttpRequest request) throws IOException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // that's ok
        }
        return super.createResponse(request);
    }
}
