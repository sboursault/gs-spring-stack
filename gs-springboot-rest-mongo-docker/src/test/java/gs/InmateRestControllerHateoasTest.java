
package gs;


import gs.repository.InmateRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static gs.InmateExamples.poisonIvy;
import static gs.InmateExamples.theJoker;
import static gs.InmateExamples.thePenguin;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InmateRestControllerHateoasTest extends AbstractRestControllerTest {

    @Autowired
    InmateRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void start() throws Exception {
        mockMvc.perform(
                        get("/"))
                .andDo(
                        print())
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$._links.inmates.href", is("http://localhost/inmates")));
    }

    @Test
    public void find_all() throws Exception {

        repository.save(thePenguin().id("penguin_1234").build());
        repository.save(theJoker().id("joker_5555").build());
        repository.save(poisonIvy().id("poisonIvy_7777").build());

        mockMvc.perform(
                        get("/inmates"))
                .andDo(
                        print())
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$._embedded[?(@.id == 'joker_5555')]._links.self.href",
                                contains("http://localhost/inmates/joker_5555")))
                .andExpect(
                        jsonPath("$._links.collection.href", is("http://localhost/inmates")))
                .andExpect(
                        jsonPath("$._links.start.href", is("http://localhost/")));
    }

    @Test
    public void find_one() throws Exception {

        repository.save(thePenguin().id("penguin_1234").build());

        mockMvc.perform(
                        get("/inmates/penguin_1234"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$._links.self.href", is("http://localhost/inmates/penguin_1234")))
                .andExpect(
                        jsonPath("$._links.collection.href", is("http://localhost/inmates")))
                .andExpect(
                        jsonPath("$._links.start.href", is("http://localhost/")));
    }

    @Test
    public void post_returns_the_entity_location() throws Exception {
        MvcResult result = mockMvc.perform(
                        postJson("/inmates").content("{\"firstname\": \"Harvey\", \"lastname\": \"Dent\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", not(isEmptyString())))
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(jsonPath("$.firstname", is("Harvey")));
    }


}