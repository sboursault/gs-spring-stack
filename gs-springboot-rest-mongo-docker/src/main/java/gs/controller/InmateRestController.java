package gs.controller;

import gs.controller.mapper.InmateMapper;
import gs.controller.resource.InmateResource;
import gs.controller.resource.InmatesResource;
import gs.exception.InmateNotFoundException;
import gs.exception.InvalidStateException;
import gs.model.Inmate;
import gs.service.InmateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static gs.util.ValidationUtils.validate;
import static gs.util.ValidationUtils.validateNotNull;
import static gs.util.ValidationUtils.validateNull;

/**
 * <p>A simple rest controller to expose inmates.</p>
 */
@RestController
@RequestMapping("/inmates")
@Api(description = "inmates API")
public class InmateRestController {

    @Autowired
    private InmateService inmateService;

    @GetMapping
    public InmatesResource findAll(/*Pageable pageable*/) {
        return convertToResourceList(inmateService.findAll(/*pageable*/));
    }

    @GetMapping(path = "/{id}")
    public InmateResource findById(@PathVariable("id") String id) throws InmateNotFoundException {
        return convertToResource(inmateService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InmateResource> create(@RequestBody InmateResource input, Errors errors) throws InvalidStateException {
        Inmate entity = convertToModel(input);
        validateNull(entity.getId(), "id", errors);
        Inmate persisted = inmateService.register(entity);
        InmateResource response = convertToResource(persisted);
        return ResponseEntity
                .created(response.getUri())
                .body(response);
    }

    @PutMapping("/{id}")
    public InmateResource update(@PathVariable("id") String id, @RequestBody InmateResource input, Errors errors) throws InvalidStateException, InmateNotFoundException {
        //FIXME: a creation date would be erased.
        Inmate entity = convertToModel(input);
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(id);
        } else {
            validate(Objects.equals(id, entity.getId()), "id", "inconsistant ids between the url and the payload", errors);
        }
        validateNotNull(entity.getId(), "id", errors);
        validateInmateExists(id); // TODO: could be in the service
        Inmate persisted = inmateService.register(entity);
        return convertToResource(persisted);
    }

    // helpers

    private void validateInmateExists(String id) throws InmateNotFoundException {
        inmateService.findById(id);
    }

    private InmateResource convertToResource(Inmate source) {
        InmateResource target = InmateMapper.INSTANCE.map(source);
        target.add(Link.toInmate(source.getId()));
        target.add(Link.toInmateCollection());
        target.add(Link.toStart());
        return target;
    }

    private Inmate convertToModel(InmateResource source) {
        return InmateMapper.INSTANCE.map(source);
    }

    private InmatesResource convertToResourceList(List<Inmate> sources) {
        List<InmateResource> resources = sources.stream()
                .map(InmateMapper.INSTANCE::map)
                .collect(Collectors.toList());
        resources.forEach(it -> it.add(Link.toInmate(it.getInmateId())));
        InmatesResource target = new InmatesResource(resources);
        target.add(Link.toInmateCollection());
        target.add(Link.toStart());
        return target;
    }

}
