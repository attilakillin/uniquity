package attilakillin.uniquitybackend.dtos;

import java.util.Collection;

/**
 * A DTO for Java collections that can be sent as a JSON object.
 * Contains the content of the collection, as well its length
 * as a separate field.
 */
public class ListDTO<T> {
    /**
     * The length of the wrapped collection.
     */
    private final long count;

    /**
     * The content of the wrapped collection.
     */
    private final Collection<T> content;

    /**
     * Creates a DTO wrapper around the given collection.
     * @param content The collection to wrap.
     */
    public ListDTO(Collection<T> content) {
        this.content = content;
        this.count = content.size();
    }

    /**
     * Retrieves the count field of the DTO.
     * @return The length of the wrapped collection.
     */
    public long getCount() {
        return count;
    }

    /**
     * Retrieves the content field of the DTO.
     * @return The content of the wrapped collection.
     */
    public Collection<T> getContent() {
        return content;
    }
}
