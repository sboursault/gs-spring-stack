package gs.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class Aka {

    @ApiModelProperty(example = "Scarecrow")
    private String name;
}
