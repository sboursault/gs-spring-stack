package gs.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gs.model.Inmate;
import org.springframework.hateoas.ResourceSupport;

import java.net.URI;

/*
 * Holds hypermedia links for the root resource.
 */
public class RootResource extends ResourceSupport {

	public RootResource() {
		add(Link.toInmateCollection().withRel("inmate-collection"));
	}

	public String getTest() {
		return "test";
	}

}
