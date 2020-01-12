package dev.mammad.simplelistapplication.config;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Responsible for Externalizing app configurations. This way we can provide different configurations
 * for different environments.
 */
public class Configurations {

    /**
     * The configuration key for the API Base URL.
     */
    private static final String PRODUCTS_BASE_URL = "products.base-url";

    /**
     * Configuration key for the HTTP read timeout.
     */
    private static final String READ_TIMEOUT = "http.read_timeout_secs";

    /**
     * Configuration key for the connection timeout.
     */
    private static final String CONNECT_TIMEOUT = "http.connect_timeout_sec";

    /**
     * Encapsulates configurations for the main environment.
     */
    private static Properties properties = readProperties("res/raw/app.properties");

    /**
     * Encapsulates the configuration for the test environment.
     */
    private static Properties testProperties = readProperties("res/raw/app_test.properties");

    /**
     * @return The API base URL for the current active profile.
     */
    public static String getBaseUrl() {
        return getByKey(PRODUCTS_BASE_URL);
    }

    /**
     * @return The read timeout in seconds in the current profile.
     */
    public static int getReadTimeout() {
        return Integer.parseInt(getByKey(READ_TIMEOUT));
    }

    /**
     * @return The connection timeout in seconds in the current profile.
     */
    public static int getConnectTimeout() {
        return Integer.parseInt(getByKey(CONNECT_TIMEOUT));
    }

    /**
     * Extract a configuration value for the given {@code key} in the current active profile.
     *
     * @param key The configuration property key.
     * @return The corresponding configuration value.
     */
    private static String getByKey(String key) {
        String property = System.getProperty("test-profile");
        return property != null ? testProperties.getProperty(key) : properties.getProperty(key);
    }

    /**
     * Load the properties file from the given {@code path}.
     *
     * @param path The properties file path.
     * @return The loaded properties instance.
     */
    @SuppressWarnings("ConstantConditions")
    private static Properties readProperties(String path) {
        InputStream resource = Configurations.class.getClassLoader().getResourceAsStream(path);
        try {
            Properties properties = new Properties();
            properties.load(resource);

            return properties;
        } catch (IOException e) {
            Log.d("Configuration Holder", "Failed to load the application properties");
            throw new RuntimeException(e);
        }
    }
}
