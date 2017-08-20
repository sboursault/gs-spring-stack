package gs.controller;

import gs.repository.InmateRepository;
import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import gs.model.Inmate;
import gs.util.RestPreconditions;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
        Inmate inmate = inmateRepository.findOne(id);
        if (inmate == null) {
            throw new InmateNotFoundException(id);
        }
        return new InmateResource(inmate);
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

    private void validate(Inmate inmate, Errors errors, RequestMethod operation) throws InvalidDataException {
        if (operation == POST) {
            RestPreconditions.checkNull("id", inmate.getId(), errors);
        } else {
            RestPreconditions.checkNotNull("id", inmate.getId(), errors);
        }
        RestPreconditions.checkNotEmpty("lastname", inmate.getLastname(), errors);
        RestPreconditions.checkNotEmpty("firstname", inmate.getFirstname(), errors);
        if (errors.hasErrors()) {
            throw new InvalidDataException(errors);
        }
    }

}
