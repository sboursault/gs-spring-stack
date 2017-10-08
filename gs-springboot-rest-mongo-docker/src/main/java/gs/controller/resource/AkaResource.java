package gs.controller.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * Resource representing an aka.
 */
@Data
public class AkaResource {

    @ApiModelProperty(example = "Scarecrow")
    private String name;
}
