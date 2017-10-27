
package gs;


import gs.repository.InmateRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.web.servlet.MockMvc;

import static gs.InmateExamples.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/*
 * This class exercices the inmate api to generate snippets for documentation purposes
 */
@AutoConfigureRestDocs(outputDir = "build/snippets/inmates")
public class InmateRestControllerDocumentation extends AbstractRestControllerTest {

    @Autowired
    InmateRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void find_one_success() throws Exception {

        repository.save(thePenguin().id("penguin_1234").build());

        mockMvc.perform(
                        get("/inmates/penguin_1234"))
                .andDo(print())
                .andDo(document("fields",
                        responseFields(
                                fieldWithPath("id").ignored(),
                                fieldWithPath("firstname").description("the inmate's firstname."),
                                fieldWithPath("lastname").description("the inmate's firstname."),
                                fieldWithPath("birthDate").description("the inmate's birthDate.").optional(),
                                fieldWithPath("aka").description("also known as...").optional(),
                                fieldWithPath("_links").ignored())
                        ));
    }


}