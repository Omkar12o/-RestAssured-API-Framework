package api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads key/value pairs from src/test/resources/config.properties
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key '" + key + "' not found in config.properties");
        }
        return value;
    }
}
