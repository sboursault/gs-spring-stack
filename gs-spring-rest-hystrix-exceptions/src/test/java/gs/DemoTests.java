package gs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static gs.util.ResponseCreatorWithWait.withSuccessAfter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsAnything.anything;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoTests {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void remote_service_fault() {

        mockServer.expect(requestTo("http://remote/service"))
                .andRespond(withServerError());

        ResponseEntity<String> response = testRestTemplate.getForEntity("/demo/1234", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void remote_service_lags() {

        mockServer.expect(requestTo("http://remote/service"))
                .andRespond(withSuccessAfter(20000));

        ResponseEntity<String> response = testRestTemplate.getForEntity("/demo/1234", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void remote_service_contingency() throws Exception {

        mockServer.expect(requestTo("http://remote/service"))
                .andRespond(withBadRequest());

        ResponseEntity<String> response = testRestTemplate.getForEntity("/demo/0000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getJsonBody(response).getString("message")).isEqualTo("unknown code");
    }

    @Test
    public void invalid_input() throws Exception {

        ResponseEntity<String> response = testRestTemplate.getForEntity("/demo/11", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getJsonBody(response).getString("message")).isEqualTo("invalid code");
    }

    // helpers

    private JSONObject getJsonBody(ResponseEntity<String> response) throws JSONException {
        return new JSONObject(response.getBody());
    }

}
