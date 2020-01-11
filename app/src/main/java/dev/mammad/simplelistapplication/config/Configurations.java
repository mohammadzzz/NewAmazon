package dev.mammad.simplelistapplication.config;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurations {

    private static final String PRODUCTS_BASE_URL = "products.base-url";
    private static final String READ_TIMEOUT = "http.read_timeout_secs";
    private static final String CONNECT_TIMEOUT = "http.connect_timeout_sec";

    private static Properties properties = readProperties("res/raw/app.properties");
    private static Properties testProperties = readProperties("res/raw/app_test.properties");

    public static String getBaseUrl() {
        return getByKey(PRODUCTS_BASE_URL);
    }

    public static int getReadTimeout() {
        return Integer.parseInt(getByKey(READ_TIMEOUT));
    }

    public static int getConnectTimeout() {
        return Integer.parseInt(getByKey(CONNECT_TIMEOUT));
    }

    private static String getByKey(String key) {
        String property = System.getProperty("test-profile");
        return property != null ? testProperties.getProperty(key) : properties.getProperty(key);
    }

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
