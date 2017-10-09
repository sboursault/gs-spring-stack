
package gs;


import com.google.common.collect.Lists;
import gs.model.Aka;
import gs.repository.InmateRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static gs.InmateExamples.poisonIvy;
import static gs.InmateExamples.theJoker;
import static gs.InmateExamples.thePenguin;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void find_all() throws Exception {

        repository.save(thePenguin().id("penguin_1234").build());
        repository.save(theJoker().id("joker_5555").build());
        repository.save(poisonIvy().id("poisonIvy_7777").build());

        mockMvc.perform(
                        get("/inmates"))
                .andDo(print())
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$._embedded[*].id",
                                containsInAnyOrder("penguin_1234", "joker_5555", "poisonIvy_7777")));
    }

    @Test
    public void find_one_success() throws Exception {

        repository.save(thePenguin().id("penguin_1234").build());

        mockMvc.perform(
                        get("/inmates/penguin_1234"))
                .andDo(print())
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.firstname", is("Oswald")));
    }

    @Test
    public void find_one_unknown() throws Exception {
        mockMvc.perform(
                        get("/inmates/calendar_man_5678"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "no inmate found with id calendar_man_5678")));
    }

    @Test
    public void post_success() throws Exception { // also verifies serialization/deserialization
        MvcResult result = mockMvc.perform(
                        postJson("/inmates").content("{" +
                                "\"firstname\": \"Harvey\"," +
                                "\"lastname\": \"Dent\"," +
                                "\"birthDate\": \"1953.01.25\"," +
                                "\"aka\": [{\"name\": \"Two-Face\"}]" +
                                "}"))
                .andDo(
                        print())
                .andExpect(
                        status().isCreated())
                .andExpect(
                        jsonPath("$.id", not(isEmptyString())))
                .andExpect(
                        jsonPath("$.firstname", is("Harvey")))
                .andExpect(
                        jsonPath("$.lastname", is("Dent")))
                .andExpect(
                        jsonPath("$.birthDate", is("1953.01.25")))
                .andExpect(
                        jsonPath("$.aka[*].name", containsInAnyOrder("Two-Face")))
                .andReturn();

        String id = (String) new JSONObject(result.getResponse().getContentAsString()).get("id");

        mockMvc.perform(
                        get("/inmates/" + id))
                .andExpect(
                        jsonPath("$.firstname", is("Harvey")));
    }

    @Test
    public void post_incomplete() throws Exception {
        mockMvc.perform(
                        postJson("/inmates").content("{}"))
                .andDo(
                        print())
                .andExpect(
                        status().isUnprocessableEntity())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "firstname must not be null or empty",
                                "lastname must not be null or empty")));
    }

    @Test
    public void post_unknown_field() throws Exception {
        mockMvc.perform(
                        postJson("/inmates").content("{" +
                                "\"firstname\": \"Harvey\"," +
                                "\"lastname\": \"Dent\"," +
                                "\"unknown_field\": \"35\"" +
                                "}"))
                .andExpect(
                        status().isCreated())
                .andExpect(
                        jsonPath("$.id", not(isEmptyString())))
                .andExpect(
                        jsonPath("$.firstname", is("Harvey")))
                .andExpect(
                        jsonPath("$.lastname", is("Dent")));
    }

    @Test
    public void post_with_no_content() throws Exception {
        mockMvc.perform(
                postJson("/inmates"))
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    public void post_invalid_date() throws Exception {
        mockMvc.perform(
                        postJson("/inmates").content("{" +
                                "\"firstname\": \"Harvey\"," +
                                "\"lastname\": \"Dent\"," +
                                "\"birthDate\": \"2000_01_01\"" +
                                "}"))
                .andDo(print())
                .andExpect(
                        status().isBadRequest())
                .andExpect(
                        jsonPath("$[0].message", containsString(
                                "Text '2000_01_01' could not be parsed at index 4")));
    }

    @Test
    public void update_success() throws Exception {

        repository.save(theJoker()
                .id("joker_5555")
                .aka(Lists.newArrayList(Aka.builder().name("Joker").build()))
                .build());

        mockMvc.perform(
                putJson("/inmates/joker_5555").content("{" +
                        "\"firstname\": \"unknown firstname\"," +
                        "\"lastname\": \"unknown lastname\"}"))
                .andDo(print())
                .andExpect(
                        jsonPath("$.id", is("joker_5555")))
                .andExpect(
                        jsonPath("$.firstname", is("unknown firstname")))
                .andExpect(
                        jsonPath("$.lastname", is("unknown lastname")))
                .andExpect(
                        jsonPath("$.aka[*].name", is(empty()))); // aka names are erased
    }

    @Test
    public void update_unknown() throws Exception {
        mockMvc.perform(
                        putJson("/inmates/joker_5555").content("{" +
                                "\"firstname\": \"unknown\"," +
                                "\"lastname\": \"unknown\"}"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "no inmate found with id joker_5555")));
    }


    @Test
    public void update_incomplete() throws Exception {

        repository.save(theJoker()
                .id("joker_5555")
                .aka(Lists.newArrayList(Aka.builder().name("Joker").build()))
                .build());

        mockMvc.perform(
                        putJson("/inmates/joker_5555")
                                .content("{\"lastname\": \"unknown lastname\"}"))
                .andDo(print())
                .andExpect(
                        status().isUnprocessableEntity())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "firstname must not be null or empty")));
    }

    @Test
    public void update_inconsistant() throws Exception {
        repository.save(theJoker()
                .id("joker_5555")
                .aka(Lists.newArrayList(Aka.builder().name("Joker").build()))
                .build());

        mockMvc.perform(
                        putJson("/inmates/joker_5555").content("{" +
                                "\"id\": \"joker_3333\"," +
                                "\"firstname\": \"unknown\"," +
                                "\"lastname\": \"unknown\"}"))
                .andDo(print())
                .andExpect(
                        status().isUnprocessableEntity())
                .andExpect(
                        jsonPath("$[*].message", containsInAnyOrder(
                                "inconsistant ids between the url and the payload")));
    }

}