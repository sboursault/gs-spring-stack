package gs;

import gs.exception.InmateNotFoundException;
import gs.exception.InvalidDataException;
import gs.util.RestPreconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A simple rest controller to expose inmates.</p>
 */


// GERER POST PUT GET PATCH, LOCATION après la creation
    // voir aussi swagger, spring rest docs

@RestController
@RequestMapping("/inmates")
public class InmateRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(InmateRestController.class);

    @Autowired
    private InmateRepository inmateRepository;

    @GetMapping("/{id}")
    public Inmate get(@PathVariable("id") String id) throws InmateNotFoundException {
        Inmate inmate = inmateRepository.findOne(id);
        if (inmate == null) {
            throw new InmateNotFoundException(id);
        }
        return inmate;
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody Inmate inmate) throws InvalidDataException {
        validate(inmate, RequestMethod.POST);
        return null;
        //TEST THIS !!!!

        //return this.accountRepository
        //        .findByUsername(userId)
        //        .map(account -> {
        //            Bookmark result = bookmarkRepository.save(new Bookmark(account,
        //                    input.uri, input.description));
//
        //            URI location = ServletUriComponentsBuilder
        //                    .fromCurrentRequest().path("/{id}")
        //                    .buildAndExpand(result.getId()).toUri();
//
        //            return ResponseEntity.created(location).build();
        //        })
        //        .orElse(ResponseEntity.noContent().build());

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
