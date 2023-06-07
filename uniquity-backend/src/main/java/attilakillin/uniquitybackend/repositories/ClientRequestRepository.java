package attilakillin.uniquitybackend.repositories;

import attilakillin.uniquitybackend.entities.ClientRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
}
