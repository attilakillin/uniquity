package attilakillin.uniquitybackend.model;

import java.util.Collection;

public class HistoryResponse {
    private final long count;
    private final Collection<RequestResponse> requests;

    public HistoryResponse(Collection<RequestResponse> requests) {
        this.requests = requests;
        this.count = requests.size();
    }

    public long getCount() {
        return count;
    }

    public Collection<RequestResponse> getRequests() {
        return requests;
    }
}
