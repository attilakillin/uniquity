package attilakillin.uniquitybackend.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UniquityService {

    /**
     * Retrieve a list of all unique file names present under the given folder
     * (and its subfolders) with the given extension.
     * <br>
     * As symbolic links complicate the task somewhat, they are neither traversed,
     * nor are their files included in the final result.
     *
     * @param folder The absolute path of the folder to search.
     * @param extension The extension to match. May start with a '.' character.
     * @return A collection of unique file names.
     */
    public Collection<String> listUniqueNames(
            String folder,
            String extension
    ) throws IllegalArgumentException, IOException {
        // Check and normalize incoming parameters.
        Path root = Paths.get(folder);
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("Specified path is not a directory!");
        }

        // Normalize extension so that it contains a starting period.
        // We want to make sure that whatever the input was, it only
        // matches files that have this *as their extension*.
        String normalizedExtension = (extension.startsWith(".")) ? extension : ('.' + extension);

        // Final result set that will be returned.
        Set<String> result;

        // Recursively walk file tree starting from the folder 'root'.
        // Files.walk() ignores symbolic links by default, which is good.
        try (Stream<Path> walk = Files.walk(root)) {
            result = walk
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(normalizedExtension))
                    .collect(Collectors.toSet());
        }

        return result;
    }
}
