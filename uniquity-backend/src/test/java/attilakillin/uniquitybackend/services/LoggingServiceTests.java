package attilakillin.uniquitybackend.services;

import attilakillin.uniquitybackend.entities.Request;
import attilakillin.uniquitybackend.repositories.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class LoggingServiceTests {

    private final RequestRepository repo = mock(RequestRepository.class);
    private final LoggingService service = new LoggingService(repo);

    @Test
    public void logging_saves_to_repo() {
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        service.logClientRequest("pdf");

        verify(repo).save(any());
    }

    @Test
    public void logging_saves_with_valid_extension() {
        when(repo.save(any())).then(i -> {
            Request param = i.getArgument(0);
            Assertions.assertEquals("pdf", param.getExtension());
            return param;
        });

        service.logClientRequest("pdf");

        verify(repo).save(any());
    }

    @Test
    public void logging_saves_with_valid_timestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        when(repo.save(any())).then(i -> {
            Request param = i.getArgument(0);
            Assertions.assertTrue(param.getTimestamp().after(now));
            return param;
        });

        service.logClientRequest("pdf");

        verify(repo).save(any());
    }

    @Test
    public void history_returns_empty() {
        when(repo.findAll()).thenReturn(new ArrayList<>());

        Collection<Request> result = service.getHistoricalRequests();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void history_returns_content() {
        when(repo.findAll()).thenReturn(List.of(
                new Request("host", new Timestamp(System.currentTimeMillis()), "ext")));

        Collection<Request> result = service.getHistoricalRequests();

        Assertions.assertEquals(1, result.size());
    }
}
