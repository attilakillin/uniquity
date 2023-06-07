package attilakillin.uniquitybackend.services;

import attilakillin.uniquitybackend.entities.ClientRequest;
import attilakillin.uniquitybackend.repositories.ClientRequestRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Collection;

@Service
public class LoggingService {
    private final ClientRequestRepository repository;

    public LoggingService(ClientRequestRepository repository) {
        this.repository = repository;
    }

    public void logClientRequest(String extension) {
        ClientRequest request = new ClientRequest(
            getHostUser(),
            new Timestamp(System.currentTimeMillis()),
            extension
        );

        repository.save(request);
    }

    public Collection<ClientRequest> getHistoricalRequests() {
        return repository.findAll();
    }

    private String getHostUser() {
        try {
            ProcessBuilder pb = new ProcessBuilder().command("whoami");
            Process process = pb.start();
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String user = reader.readLine();
            reader.close();
            return user;
        } catch (IOException | InterruptedException ex) {
            return "unknown";
        }
    }
}
