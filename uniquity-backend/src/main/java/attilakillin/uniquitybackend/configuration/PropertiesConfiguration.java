package attilakillin.uniquitybackend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Enable configuration properties from application.properties/yaml.
 */
@Configuration
@ConfigurationProperties(prefix = "application")
public class PropertiesConfiguration {
    /**
     * The root folder in which the server instances will search files.
     */
    private String rootFolder;

    /**
     * Set the rootFolder field.
     * @param rootFolder The root folder in which the server instances should search files.
     */
    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    /**
     * Retrieve the rootFolder field.
     * @return The root folder in which the server instances will search files.
     */
    public String getRootFolder() {
        return rootFolder;
    }
}
