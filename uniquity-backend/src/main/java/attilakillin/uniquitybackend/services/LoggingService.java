package attilakillin.uniquitybackend.services;

import attilakillin.uniquitybackend.entities.ClientRequest;
import attilakillin.uniquitybackend.repositories.ClientRequestRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class LoggingService {
    private final ClientRequestRepository repository;

    public LoggingService(ClientRequestRepository repository) {
        this.repository = repository;
    }

    public void logClientRequest(String extension) {
        ClientRequest request = new ClientRequest(
            "",
            new Timestamp(System.currentTimeMillis()),
            extension
        );

        repository.save(request);
    }
}
