package gs;


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
    private String id;

    private String lastname;
    private String firstname;

    private List<String> aka;
}
