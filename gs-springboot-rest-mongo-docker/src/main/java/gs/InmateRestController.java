package gs;

import gs.controller.InmateResource;
import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import gs.model.Inmate;
import gs.util.RestPreconditions;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A simple rest controller to expose inmates.</p>
 */
@RestController
@RequestMapping("/inmates")
@Api(description = "inmates API")
public class InmateRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(InmateRestController.class);

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
    ResponseEntity<Inmate> create(@RequestBody Inmate entity) throws InvalidDataException {
        validate(entity, RequestMethod.POST);
        Inmate persisted = inmateRepository.save(entity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(persisted.getId()).toUri();

        return ResponseEntity.created(location)
                .body(entity);
    }

    private void validate(Inmate inmate, RequestMethod operation) throws InvalidDataException {
        List<String> errors = new ArrayList<>();
        if (operation == RequestMethod.POST) {
            RestPreconditions.checkNull(inmate.getId(), "id must be null", errors);
        } else {
            RestPreconditions.checkNotNull(inmate.getId(), "id must not be null", errors);
        }
        RestPreconditions.checkNotEmpty(inmate.getLastname(), "lastname must not be empty", errors);
        RestPreconditions.checkNotEmpty(inmate.getFirstname(), "firstname must not be empty", errors);
        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

}
