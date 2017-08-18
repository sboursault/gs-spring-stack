package gs.repository;

import gs.model.Inmate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InmateRepository extends MongoRepository<Inmate, String> {

    Inmate findByAka(String aka);

}