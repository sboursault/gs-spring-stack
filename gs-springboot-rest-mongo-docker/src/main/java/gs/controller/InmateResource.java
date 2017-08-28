package gs.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gs.model.Inmate;
import org.springframework.hateoas.ResourceSupport;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
/*
 * Wrapper for the inmate entity.
 * It adds hypermedia links to the wrapped inmate, the inmate collection, etc.
 */
public class InmateResource extends ResourceSupport {

	private final Inmate inmate;

	public InmateResource(Inmate entity) {
		this.inmate = entity;
		add(Link.toInmate(entity.getId()));
		add(Link.toInmateCollection());
		add(Link.toStart());
	}

	@JsonUnwrapped // avoid having inmate details in a sub node
	public Inmate getInmate() {
		return inmate;
	}

	@JsonIgnore
	public URI getUri() {
		org.springframework.hateoas.Link link = getId();
		return link == null ? null : URI.create(link.getHref());
	}
}
