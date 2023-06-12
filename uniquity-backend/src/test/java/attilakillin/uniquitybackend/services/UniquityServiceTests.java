package attilakillin.uniquitybackend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class UniquityServiceTests {
    private final UniquityService service = new UniquityService();

    @TempDir
    private Path root;

    @Test
    public void returns_empty_for_empty_folder() {
        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), ""));

        // Check for emptiness
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void returns_name_for_one_file() throws IOException {
        // Create one file
        String filename = "test.txt";
        Files.write(root.resolve(filename), new ArrayList<>());

        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), "txt"));

        // Check for that one file
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(filename));
    }

    @Test
    public void returns_name_once_for_shared_file_name() throws IOException {
        // Create three files with the same name
        String filename = "test.txt";

        Files.createDirectories(root.resolve("sub1"));
        Files.createDirectories(root.resolve("sub2"));

        Files.write(root.resolve(filename), new ArrayList<>());
        Files.write(root.resolve("sub1/" + filename), new ArrayList<>());
        Files.write(root.resolve("sub2/" + filename), new ArrayList<>());

        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), "txt"));

        // Check that the result only contains one entry
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(filename));
    }

    @Test
    public void returns_names_for_multiple_files() throws IOException {
        // Create three files in different places
        String[] files = new String[] { "alpha.txt", "beta.txt", "gamma.txt" };

        Files.createDirectories(root.resolve("sub1"));
        Files.createDirectories(root.resolve("sub2"));

        Files.write(root.resolve(files[0]), new ArrayList<>());
        Files.write(root.resolve("sub1/" + files[1]), new ArrayList<>());
        Files.write(root.resolve("sub2/" + files[2]), new ArrayList<>());

        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), "txt"));

        // Check that the result contains all three
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.containsAll(Arrays.stream(files).toList()));
    }

    @Test
    public void returns_only_correct_extension_without_dot() throws IOException {
        // Create three files in different places
        String[] files = new String[] { "alpha.txt", "beta.pdf", "gamma.service" };

        Files.createDirectories(root.resolve("sub1"));
        Files.createDirectories(root.resolve("sub2"));

        Files.write(root.resolve(files[0]), new ArrayList<>());
        Files.write(root.resolve("sub1/" + files[1]), new ArrayList<>());
        Files.write(root.resolve("sub2/" + files[2]), new ArrayList<>());

        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), "pdf"));

        // Check that the result contains all three
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(files[1]));
    }

    @Test
    public void returns_only_correct_extension_with_dot() throws IOException {
        // Create three files in different places
        String[] files = new String[] { "alpha.txt", "beta.pdf", "gamma.service" };

        Files.createDirectories(root.resolve("sub1"));
        Files.createDirectories(root.resolve("sub2"));

        Files.write(root.resolve(files[0]), new ArrayList<>());
        Files.write(root.resolve("sub1/" + files[1]), new ArrayList<>());
        Files.write(root.resolve("sub2/" + files[2]), new ArrayList<>());

        // Call service method
        Collection<String> result = Assertions.assertDoesNotThrow(() ->
                service.listUniqueNames(root.toString(), ".pdf"));

        // Check that the result contains all three
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(files[1]));
    }
}
