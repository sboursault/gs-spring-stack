
package gs;


import gs.model.Inmate;
import io.swagger.annotations.Api;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static gs.InmateExamples.thePenguin;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class InmateRestControllerTest extends RestControllerTest {

    @Autowired
    InmateRepository repository;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {

        repository.deleteAll();

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void get_ok() throws Exception {

        Inmate penguin = repository.save(thePenguin().id("penguin_1234").build());

        mockMvc.perform(
                        get("/inmates/penguin_1234"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.firstname", is("Oswald")));
    }

    @Test
    public void get_unknown_inmate() throws Exception {
        mockMvc.perform(
                        get("/inmates/calendar_man_5678"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "no inmate found with id calendar_man_5678")));
    }

    @Test
    public void post_incomplete() throws Exception {
        mockMvc.perform(
                        postJson("/inmates")
                                .content("{}"))
                .andExpect(
                        status().isUnprocessableEntity())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "firstname must not be empty",
                                "lastname must not be empty")));
    }

    @Test
    public void post_returns_the_persisted_entity() throws Exception {
        MvcResult result = mockMvc.perform(
                postJson("/inmates")
                        .content("{" +
                                "\"firstname\": \"Harvey\"," +
                                "\"lastname\": \"Dent\"," +
                                "\"aka\": [{\"name\": \"Two-Face\"}]" +
                                "}"))
                .andExpect(
                        status().isCreated())
                .andExpect(
                        jsonPath("$.id", not(isEmptyString())))
                .andExpect(
                        jsonPath("$.firstname", is("Harvey")))
                .andExpect(
                        jsonPath("$.lastname", is("Dent")))
                .andExpect(
                        jsonPath("$.aka[*].name", containsInAnyOrder("Two-Face")))
                .andReturn();

        String id = (String) new JSONObject(result.getResponse().getContentAsString()).get("id");

        mockMvc.perform(
                get("/inmates/" + id))
                .andExpect(jsonPath("$.firstname", is("Harvey")));
    }
}