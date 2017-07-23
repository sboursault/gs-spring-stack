package gs;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>A simple controller, it exposes a "/demo" end point which calls a hystrix command.</p>
 */
@RestController
public class DemoRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DemoRestController.class);

    @Autowired
    private RemoteServiceAdapter adapter;

    @GetMapping("/demo/{code}")
    public void demo(@PathVariable("code") String code) {

        validate(code);
        LOGGER.info("start processing GET /demo");
        try {
            adapter.callRemote(code);
        } catch(HttpClientErrorException e) {
            throw new InvalidCodeException("unknown code");
        } catch(Exception e ) {
            // catches HttpServerErrorException, UndeclaredThrowableException due to TimeoutException, etc.
            throw new RemoteServiceFault();
        }
        LOGGER.info("end processing GET /demo");
    }

    private void validate(String code) {
        if (!code.matches("^\\d{4}$")
                || Integer.valueOf(code) > 9000) {
            throw new InvalidCodeException("invalid code");
        }
    }

}
