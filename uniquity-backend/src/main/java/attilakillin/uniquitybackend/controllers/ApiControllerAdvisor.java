package attilakillin.uniquitybackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * Controller advice for the ApiController endpoints.
 * Handles uncaught exceptions that occur while serving requests.
 */
@ControllerAdvice
public class ApiControllerAdvisor extends ResponseEntityExceptionHandler {
    /**
     * Logging instance to pring useful information to server log.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handle illegal argument exceptions. These generally mean an incorrectly configured
     * server instance.
     * @param ex The thrown exception.
     * @param request The request during which the exception was thrown.
     * @return The response the client should receive.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentExceptions(
        IllegalArgumentException ex, WebRequest request
    ) {
        logger.error("Server configured incorrectly, received IllegalArgumentException:");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle IO exceptions. These can come from a number of sources.
     * @param ex The thrown exception.
     * @param request The request during which the exception was thrown.
     * @return The response the client should receive.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Void> handleIOExceptions(
        IOException ex, WebRequest request
    ) {
        logger.error("An otherwise unhandled IO exception occurred:");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
