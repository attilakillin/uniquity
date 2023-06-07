package attilakillin.uniquitybackend.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * This service class implements a file system traversal solutino that lists all unique
 * file names under a given directory.
 */
@Service
public class UniquityService {

    /**
     * Retrieve a list of all unique file names present under the given folder
     * (and its subfolders) with the given extension.
     * <br>
     * As symbolic links complicate the task somewhat, they are neither traversed,
     * nor are their files included in the final result.
     * <br>
     * The only exception is the starting folder. If the path of the starting folder
     * contains links, those are followed.
     * <br>
     * If the file system denies access to specific folders, those are skipped
     * automatically, and do not result in a thrown exception.
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
            throw new IllegalArgumentException("Specified path (" + folder + ") is not a directory!");
        }

        // Normalize extension so that it contains a starting period.
        // We want to make sure that whatever the input was, it only
        // matches files that have this *as their extension*.
        String normalizedExtension = (extension.startsWith(".")) ? (extension) : ('.' + extension);

        // Initialize result list.
        Set<String> result = new HashSet<>();

        // Recursively walk file tree starting from the folder 'root'.
        // Files.walkFileTree() ignores symbolic links by default, which is good.
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // If it's a regular file, and has the required extension, we add it to our result list.
                if (attrs.isRegularFile()) {
                    String name = file.getFileName().toString();
                    if (name.endsWith(normalizedExtension)) {
                        result.add(name);
                    }
                }

                // Continue by default.
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException ex) throws IOException {
                // If we were denied access by the file system, we'll skip the subtree.
                if (ex instanceof AccessDeniedException) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                // Else rethrow the exception.
                return super.visitFileFailed(file, ex);
            }
        });

        // Return the set we built.
        return result;
    }
}
