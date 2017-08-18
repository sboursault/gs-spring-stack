package gs.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gs.InmateRestController;
import gs.model.Inmate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class InmateResource extends ResourceSupport {

	private static Logger LOGGER = LoggerFactory.getLogger(InmateResource.class);

	private final Inmate inmate;

	public InmateResource(Inmate entity) {
		this.inmate = entity;

		//URI location = ServletUriComponentsBuilder
		//		.fromCurrentRequest().path("/{id}")
		//		.buildAndExpand(entity.getId()).toUri();

		try {
			add(linkTo(InmateRestController.class.getMethod("get", String.class), inmate.getId()).withSelfRel());
		} catch (NoSuchMethodException e) {
			LOGGER.warn("failed to add rel link self on inmate ({})", e.getMessage());
		}
		//TODO: add link rel "collection"
	}

	@JsonUnwrapped // avoid having inmate details in a sub node
	public Inmate getInmate() {
		return inmate;
	}

	@JsonIgnore
	public URI getLinkSelfAsUri() {
		Link link = getLink(Link.REL_SELF);
		return link == null ? null : URI.create(link.getHref());
	}
}
