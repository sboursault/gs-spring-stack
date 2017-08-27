package gs.controller;

import gs.exception.InmateNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Factory class to create inmate links.
 */
public class InmateLink {

    private static final String REL_COLLECTION = "collection";

    protected static Link toEntity(String id) {
        try {
            return linkTo(methodOn(InmateRestController.class).findById(id)).withSelfRel();
        } catch (InmateNotFoundException e) {
            // can't happen: a dummy InmateRestController is used
            throw new RuntimeException(e);
        }
    }

    protected static Link toCollection() {
        return linkTo(methodOn(InmateRestController.class).findAll()).withRel(REL_COLLECTION);
    }

}
