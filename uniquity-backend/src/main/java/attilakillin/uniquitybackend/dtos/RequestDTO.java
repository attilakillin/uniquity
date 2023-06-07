package attilakillin.uniquitybackend.dtos;

import attilakillin.uniquitybackend.entities.Request;

/**
 * A DTO containing the public fields of a logged request.
 */
public class RequestDTO {
    /**
     * The host user of the server instance that executed the request.
     */
    private final String host;

    /**
     * The exact moment when the request was received, as a formatted string.
     */
    private final String timestamp;

    /**
     * The file extension that the request query contained.
     */
    private final String extension;

    /**
     * A constructor that creates the DTO from the fields of a Request entity.
     * @param original The entity to create the DTO from.
     */
    public RequestDTO(Request original) {
        this.host = original.getHost();
        this.timestamp = original.getTimestamp().toString();
        this.extension = original.getExtension();
    }

    /**
     * Retrieves the host field of the DTO.
     * @return The host user of the server instance that executed the request.
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieves the timestamp field of the DTO.
     * @return The exact moment when the request was received, as a formatted string.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the extension field of the DTO.
     * @return The file extension that the request query contained.
     */
    public String getExtension() {
        return extension;
    }
}
