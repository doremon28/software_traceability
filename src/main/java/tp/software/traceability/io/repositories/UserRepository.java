package tp.software.traceability.io.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tp.software.traceability.io.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

}
