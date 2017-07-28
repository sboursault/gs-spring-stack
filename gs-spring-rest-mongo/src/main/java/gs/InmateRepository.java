package gs;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InmateRepository extends MongoRepository<Inmate, String> {

    Inmate findByAka(String aka);

}