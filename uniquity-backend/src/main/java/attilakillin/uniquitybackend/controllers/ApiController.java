package attilakillin.uniquitybackend.controllers;

import attilakillin.uniquitybackend.configuration.PropertiesConfiguration;
import attilakillin.uniquitybackend.dtos.ListDTO;
import attilakillin.uniquitybackend.dtos.RequestDTO;
import attilakillin.uniquitybackend.services.LoggingService;
import attilakillin.uniquitybackend.services.UniquityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collection;

/**
 * The main API controller of the application.
 * Contains two endpoints (specified below) that implement various tasks.
 */
@Controller
@RequestMapping("/api")
public class ApiController {
    /**
     * Logging service class.
     */
    private final LoggingService loggingService;
    /**
     * Uniquity service class.
     */
    private final UniquityService uniquityService;

    /**
     * Application-specific configuration values.
     */
    private final PropertiesConfiguration configuration;

    /**
     * Public constructor with two injected service dependencies.
     * @param loggingService An instance of the LoggingService service.
     * @param uniquityService An instance of the UniquityService service.
     */
    public ApiController(
        LoggingService loggingService,
        UniquityService uniquityService,
        PropertiesConfiguration configuration
    ) {
        this.loggingService = loggingService;
        this.uniquityService = uniquityService;
        this.configuration = configuration;
    }

    /**
     * Retrieve the list of files present under the preconfigured folder (recursively).
     * All files will only be present exactly once in the result.
     * @param extension The file extension to search for.
     * @return The list of the found files.
     */
    @GetMapping("/unique-names")
    public ResponseEntity<ListDTO<String>> getUniqueNames(
            @RequestParam(value = "extension", required = false) String extension
    ) throws IOException {
        // If the extension was null, we return a bad request response.
        if (extension == null) {
            return ResponseEntity.badRequest().build();
        }

        // Log that a request has been received.
        loggingService.logClientRequest(extension);

        // Execute request. Thrown errors are handled by the ApiControllerAdvisor class.
        Collection<String> names = uniquityService
                .listUniqueNames(this.configuration.getRootFolder(), extension);

        // Return an HTTP 200 response.
        return ResponseEntity.ok(new ListDTO<>(names));
    }

    /**
     * Retrieve the list of previous requests (which server instance executed a query,
     * when, and what extension was queried).
     *
     * @return A list of the previously made requests.
     */
    @GetMapping("/history")
    public ResponseEntity<ListDTO<RequestDTO>> getHistory() {
        // Retrieve requests and map them to DTO objects.
        Collection<RequestDTO> requests = loggingService
                .getHistoricalRequests()
                .stream()
                .map(RequestDTO::new)
                .toList();

        // Return an HTTP 200 response.
        return ResponseEntity.ok(new ListDTO<>(requests));
    }
}
