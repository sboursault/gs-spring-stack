package gs;

import com.google.common.base.Preconditions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static java.time.LocalDate.now;

/**
 * Hystrixified Adapter for a remote service
 */
@Service
public class RemoteServiceAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RemoteServiceAdapter.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand
    public String callRemote(String code) {
        Preconditions.checkNotNull(code);
        Preconditions.checkArgument(code.length() == 4);
        LOGGER.info("call remote service");
        return restTemplate.getForObject("http://remote/service", String.class);
    }

}
