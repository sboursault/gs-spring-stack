package gs.service;

import gs.exception.InmateNotFoundException;
import gs.exception.InvalidStateException;
import gs.model.Inmate;
import gs.repository.InmateRepository;
import gs.workflow.InmateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;

@Component
public class InmateService {

    @Autowired
    private InmateValidator inmateValidator;

    @Autowired
    private InmateRepository inmateRepository;

    public Inmate findById(String id) throws InmateNotFoundException {
        Inmate inmate = inmateRepository.findOne(id);
        if (inmate == null) {
            throw new InmateNotFoundException(id);
        }
        return inmate;
    }

    public List<Inmate> findAll() {
        return inmateRepository.findAll();
    }

    public Inmate register(Inmate inmate) throws InvalidStateException {
        Errors errors = new BeanPropertyBindingResult(inmate, Inmate.class.getTypeName());
        if (!inmateValidator.isValidForLockup(inmate, errors))
            throw new InvalidStateException(errors);
        return inmateRepository.save(inmate);
    }
}
