package gs.controller;

import gs.controller.mapper.InmateMapper;
import gs.controller.resource.InmateResource;
import gs.controller.resource.InmatesResource;
import gs.repository.InmateRepository;
import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import gs.model.Inmate;
import gs.util.RestPreconditions;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static gs.util.RestPreconditions.check;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * <p>A simple rest controller to expose inmates.</p>
 */
@RestController
@RequestMapping("/inmates")
@Api(description = "inmates API")
public class InmateRestController {

    @Autowired
    private InmateRepository inmateRepository;

    @GetMapping
    public InmatesResource findAll(/*Pageable pageable*/) {
        return convertToResourceList(inmateRepository.findAll(/*pageable*/));
    }

    @GetMapping(path = "/{id}")
    public InmateResource findById(@PathVariable("id") String id) throws InmateNotFoundException {
        return convertToResource(fetchInmate(id));
    }

    @PostMapping
    public ResponseEntity<InmateResource> create(@RequestBody InmateResource input, Errors errors) throws InvalidDataException {
        Inmate entity = convertToModel(input);
        validate(entity, errors, POST); // TODO: use an external validator e.g. validator.isValidForRegistration(<>)
        Inmate persisted = inmateRepository.save(entity);
        InmateResource response = convertToResource(persisted);
        return ResponseEntity
                .created(response.getUri())
                .body(response);
    }

    @PutMapping("/{id}")
    public InmateResource update(@PathVariable("id") String id, @RequestBody InmateResource input, Errors errors) throws InvalidDataException, InmateNotFoundException {
        //FIXME: a creation date would be erased.
        Inmate entity = convertToModel(input);
        validateInmateExists(id);
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(id);
        } else {
            check(Objects.equals(id, entity.getId()), "id", "inconsistant ids between the url and the payload", errors);
        }
        validate(entity, errors, PUT);
        Inmate persisted = inmateRepository.save(entity);
        return convertToResource(persisted);
    }

    // helpers

    private Inmate fetchInmate(String id) throws InmateNotFoundException {
        Inmate inmate = inmateRepository.findOne(id);
        if (inmate == null) {
            throw new InmateNotFoundException(id);
        }
        return inmate;
    }

    private void validateInmateExists(String id) throws InmateNotFoundException {
        fetchInmate(id);
    }

    private void validate(Inmate inmate, Errors errors, RequestMethod operation) throws InvalidDataException {
        if (operation == POST) {
            RestPreconditions.checkNull(inmate.getId(), "id", errors);
        } else {
            RestPreconditions.checkNotNull(inmate.getId(), "id", errors);
        }
        RestPreconditions.checkNotEmpty(inmate.getLastname(), "lastname", errors);
        RestPreconditions.checkNotEmpty(inmate.getFirstname(), "firstname", errors);
        if (errors.hasErrors()) {
            throw new InvalidDataException(errors);
        }
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
