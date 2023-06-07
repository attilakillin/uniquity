package attilakillin.uniquitybackend.model;

import attilakillin.uniquitybackend.entities.ClientRequest;

public class RequestResponse {
    private final String host;

    private final String timestamp;

    private final String extension;

    public RequestResponse(ClientRequest original) {
        this.host = original.getHost();
        this.timestamp = original.getTimestamp().toString();
        this.extension = original.getExtension();
    }


    public String getHost() {
        return host;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getExtension() {
        return extension;
    }
}
