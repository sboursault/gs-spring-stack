
package gs;


import gs.model.Inmate;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static gs.InmateExamples.thePenguin;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class InmateRestControllerHateoasTest extends RestControllerTest {

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
                        jsonPath("$._links.self.href", is("http://localhost/inmates/penguin_1234")));
    }

    @Test
    public void post_returns_the_entity_location() throws Exception {
        MvcResult result = mockMvc.perform(
                postJson("/inmates")
                        .content("{" +
                                "\"firstname\": \"Harvey\"," +
                                "\"lastname\": \"Dent\"," +
                                "\"aka\": [{\"name\": \"Two-Face\"}]" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", not(isEmptyString())))
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(jsonPath("$.firstname", is("Harvey")));
    }


}