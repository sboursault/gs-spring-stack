package gs.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import gs.model.Inmate;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
 * Wrapper for an inmate list.
 * It adds an hypermedia link to the inmate collection.
 */
public class InmatesResource extends ResourceSupport {

	private final List<InmateResource> results;

	public InmatesResource(List<Inmate> inmates) {
		results = inmates.stream()
				.map(e -> new InmateResource(e))
				.collect(toList());
		add(Link.toInmateCollection());
		add(Link.toStart());
	}

	@JsonProperty("_embedded")
	public List<InmateResource> getResults() {
		return results;
	}

}
