package attilakillin.uniquitybackend.model;

import java.util.Collection;

public class NamesResponse {
    private final long count;
    private final Collection<String> names;

    public NamesResponse(Collection<String> names) {
        this.names = names;
        this.count = names.size();
    }

    public long getCount() {
        return count;
    }

    public Collection<String> getNames() {
        return names;
    }
}
