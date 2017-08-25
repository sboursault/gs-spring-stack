package gs.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
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

    @ApiModelProperty(example = "1970.01.25", required = false, position = 3)
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate birthDate;

    // to represent aka, a simple string could do the job (e.g. "Scarecrow"),
    // but a list of objects facilitates future changes:
    // with a list of objects, we can add an aka chronology, without breaking changes for the clients.
    @ApiModelProperty(position = 4)
    private List<Aka> aka;
}
