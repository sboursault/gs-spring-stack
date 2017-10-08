package gs.controller.resource;

import gs.controller.Link;
import org.springframework.hateoas.ResourceSupport;

import java.net.URI;

/*
 * Holds hypermedia links for the root resource.
 */
public class RootResource extends ResourceSupport {

	public RootResource() {
		add(Link.toInmateCollection().withRel("inmates"));
	}

}
