
package gs;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.nio.charset.Charset;

import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AbstractRestControllerTest {

    protected MediaType APPLICATION_JSON = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    protected MockHttpServletRequestBuilder postJson(String url) {
        return post(url).contentType(APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder putJson(String url) {
        return put(url).contentType(APPLICATION_JSON);
    }

    protected RestDocumentationResultHandler document(String identifier, Snippet... snippets) {
        return MockMvcRestDocumentation.document(identifier, preprocessResponse(prettyPrint(), maskLinks(), removeHeaders(CONTENT_LENGTH)), snippets);
    }
}