package attilakillin.uniquitybackend.repositories;

import attilakillin.uniquitybackend.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Request entity.
 * Contains no additional methods, as only saving and retrieval is needed,
 * which are provided by default.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
