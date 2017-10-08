package gs.controller.resource;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Resource representing an inmate.
 * Also provides links related to the inmate.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "firstname",
        "lastname",
        "birthDate",
        "aka"
})
@Data
public class InmateResource extends ResourceSupport {

	@ApiModelProperty(example = "abcd1234", position = 0)
	@JsonProperty("id")
	private String inmateId;

	@ApiModelProperty(example = "Jonathan", required = true, position = 1)
	private String firstname;

	@ApiModelProperty(example = "Crane", required = true, position = 2)
	private String lastname;

	@ApiModelProperty(example = "1970.01.25", required = false, position = 3)
	@JsonFormat(pattern = "yyyy.MM.dd")
	private LocalDate birthDate;

	@ApiModelProperty(position = 4)
	private final List<AkaResource> aka = new ArrayList<>();

	@JsonIgnore
	public URI getUri() {
		org.springframework.hateoas.Link link = getId();
		return link == null ? null : URI.create(link.getHref());
	}

}
