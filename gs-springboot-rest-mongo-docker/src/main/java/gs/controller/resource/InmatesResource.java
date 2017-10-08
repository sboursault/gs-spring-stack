package gs.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/*
 * Wrapper for an inmate list.
 * It adds an hypermedia link to the inmate collection.
 */
public class InmatesResource extends ResourceSupport {

	private final List<InmateResource> results;

	public InmatesResource(List<InmateResource> inmates) {
		results = inmates;
	}

	@JsonProperty("_embedded")
	public List<InmateResource> getResults() {
		return results;
	}

}
