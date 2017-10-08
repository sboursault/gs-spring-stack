package gs.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inmate {

    @Id
    private String id;

    private String lastname;

    private String firstname;

    private LocalDate birthDate;

    // to represent aka, a simple string could do the job (e.g. "Scarecrow"),
    // but a list of objects facilitates future changes:
    // with a list of objects, we can add an aka chronology, without breaking changes for the clients.
    private List<Aka> aka;
}
