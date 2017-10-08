package gs.controller;

import gs.config.ApplicationContextProvider;
import gs.config.ApplicationProperties;
import gs.exception.InmateNotFoundException;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Iterator;

import static gs.config.ApplicationContextProvider.getApplicationContext;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Factory class to create inmate links.
 */
public class Link {

    private static final String REL_START = "start";
    private static final String REL_COLLECTION = "collection";

    private static final MappingDiscoverer DISCOVERER = new AnnotationMappingDiscoverer(RequestMapping.class);


    public static org.springframework.hateoas.Link toStart() {
        String href = uriTo(methodOn(RootRestController.class).start());
        return new org.springframework.hateoas.Link(href).withRel(REL_START);
    }

    public static org.springframework.hateoas.Link toInmate(String id) {
        try {
            String href = uriTo(methodOn(InmateRestController.class).findById(id), id);
            return new org.springframework.hateoas.Link(href).withSelfRel();
        } catch (InmateNotFoundException e) {
            // can't happen: a dummy InmateRestController is used
            throw new RuntimeException(e);
        }
    }

    public static org.springframework.hateoas.Link toInmateCollection() {
        String href = uriTo(methodOn(InmateRestController.class).findAll());
        return new org.springframework.hateoas.Link(href).withRel(REL_COLLECTION);
    }

    private static String uriTo(Object method, Object... pathParams) {
        return UriComponentsBuilder.fromHttpUrl(getBasePath()).path(mappingTo(method)).buildAndExpand(pathParams).toUriString();
    }

    private static String mappingTo(Object invocationValue) {
        // picked from org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo(java.lang.Object)
        Assert.isInstanceOf(DummyInvocationUtils.LastInvocationAware.class, invocationValue);
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) invocationValue;
        DummyInvocationUtils.MethodInvocation invocation = invocations.getLastInvocation();
        return DISCOVERER.getMapping(invocation.getTargetType(), invocation.getMethod());
    }

    private static String getBasePath() {
        return getApplicationContext().getBean(ApplicationProperties.class).getBaseUrl();
    }

}
