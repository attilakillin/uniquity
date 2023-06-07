package attilakillin.uniquitybackend.controllers;

import attilakillin.uniquitybackend.model.NamesResult;
import attilakillin.uniquitybackend.services.LoggingService;
import attilakillin.uniquitybackend.services.UniquityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/api")
public class ApiController {
    private final LoggingService loggingService;
    private final UniquityService uniquityService;
    private final Logger logger;

    public ApiController(
        LoggingService loggingService,
        UniquityService uniquityService
    ) {
        this.loggingService = loggingService;
        this.uniquityService = uniquityService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Operation(summary = "List unique file names under the preconfigured folder")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = NamesResult.class))
        })
    })

    @GetMapping("/unique-names")
    public ResponseEntity<NamesResult> getUniqueNames(@Param("extension") String extension) {
        loggingService.logClientRequest(extension);

        try {
            Collection<String> names = uniquityService.listUniqueNames("/home", extension);
            return ResponseEntity.ok(new NamesResult(names));

        } catch (Exception ex) {
            logger.error("An exception occurred while listing unique names:");
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
