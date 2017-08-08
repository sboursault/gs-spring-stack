package gs.controller;

import gs.InmateRestController;
import gs.model.Inmate;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class InmateResource extends ResourceSupport {

	private final Inmate inmate; // content ? entity ? item ?

	public InmateResource(Inmate entity) {
		this.inmate = entity;
		add(linkTo(methodOn(InmateRestController.class)).slash(inmate.getId()).withRel("self"));
		add(linkTo(methodOn(InmateRestController.class)).withRel("collection"));
	}

	public Inmate getInmate() {
		return inmate;
	}
}