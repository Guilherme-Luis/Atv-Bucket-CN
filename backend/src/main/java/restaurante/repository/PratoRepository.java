package restaurante.repository;

import restaurante.model.Prato;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PratoRepository extends MongoRepository<Prato, String> {

    List<Prato> findAllByOrderByNomeAsc();

    List<Prato> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

}
