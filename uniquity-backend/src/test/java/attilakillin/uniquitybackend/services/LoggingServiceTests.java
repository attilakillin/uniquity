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
        // Mimic normal behaviour
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Call service function
        service.logClientRequest("pdf");

        // Verify mock was called
        verify(repo).save(any());
    }

    @Test
    public void logging_saves_with_valid_extension() {
        // When called, assert that a valid argument was passed
        when(repo.save(any())).then(i -> {
            Request param = i.getArgument(0);
            Assertions.assertEquals("pdf", param.getExtension());
            return param;
        });

        // Call service function
        service.logClientRequest("pdf");

        // Verify mock was called
        verify(repo).save(any());
    }

    @Test
    public void logging_saves_with_valid_timestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // When called, assert that a valid argument was passed
        when(repo.save(any())).then(i -> {
            Request param = i.getArgument(0);
            Assertions.assertTrue(param.getTimestamp().after(now));
            return param;
        });

        // Call service function
        service.logClientRequest("pdf");

        // Verify mock was called
        verify(repo).save(any());
    }

    @Test
    public void history_returns_empty() {
        // Mimic behaviour
        when(repo.findAll()).thenReturn(new ArrayList<>());

        // Call service function
        Collection<Request> result = service.getHistoricalRequests();

        // Assert returned collection size
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void history_returns_content() {
        // Mimic behaviour with non-empty list
        when(repo.findAll()).thenReturn(List.of(
                new Request("host", new Timestamp(System.currentTimeMillis()), "ext")));

        // Call service function
        Collection<Request> result = service.getHistoricalRequests();

        // Assert returned collection size
        Assertions.assertEquals(1, result.size());
    }
}
