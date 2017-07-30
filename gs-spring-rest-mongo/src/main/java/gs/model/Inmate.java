package gs.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
@Data
public class Inmate {

    @Id
    @ApiModelProperty(example = "abcd1234", position = 0)
    private String id;

    @ApiModelProperty(example = "Crane", required = true, position = 1)
    private String lastname;

    @ApiModelProperty(example = "Jonathan", required = true, position = 2)
    private String firstname;

    // to represent aka, a simple string could do the job for now (e.g. "Scarecrow"),
    // but a list of objects facilitates future changes:
    // with a list of objects, we can add an aka chronology, without breaking changes for the clients.
    @ApiModelProperty(example = "abcd1234", position = 3)
    private List<Aka> aka;
}
