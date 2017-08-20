package gs.controller;

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

import java.util.Objects;

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

    @GetMapping("/{id}")
    public InmateResource get(@PathVariable("id") String id) throws InmateNotFoundException {
        return new InmateResource(fetchInmate(id));
    }

    @PostMapping
    public ResponseEntity<InmateResource> create(@RequestBody Inmate entity, Errors errors) throws InvalidDataException {
        validate(entity, errors, POST);
        Inmate persisted = inmateRepository.save(entity);
        InmateResource response = new InmateResource(persisted);
        return ResponseEntity
                .created(response.getLinkSelfAsUri())
                .body(response);
    }

    @PutMapping("/{id}")
    public InmateResource update(@PathVariable("id") String id, @RequestBody Inmate entity, Errors errors) throws InvalidDataException, InmateNotFoundException {
        validateInmateExists(id);
        if (StringUtils.isEmpty(entity.getId()))
            entity.setId(id);
        else
            check(Objects.equals(id, entity.getId()), "id", "inconsistant ids between the url and the payload", errors);
        validate(entity, errors, PUT);
        Inmate persisted = inmateRepository.save(entity);
        return new InmateResource(persisted);
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

}
