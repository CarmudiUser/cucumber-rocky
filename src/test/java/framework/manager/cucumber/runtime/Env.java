package test.java.framework.manager.cucumber.runtime;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Looks up values in the following order:
 * <ol>
 * <li>Environment variable</li>
 * <li>System property</li>
 * <li>Resource bundle</li>
 * </ol>
 */
public class Env {
    private final String bundleName;
    private final Properties properties;

    public Env() {
        this(null, System.getProperties());
    }

    public Env(String bundleName) {
        this(bundleName, System.getProperties());
    }

    public Env(Properties properties) {
        this(null, properties);
    }

    public Env(String bundleName, Properties properties) {
        this.bundleName = bundleName;
        this.properties = properties;
    }

    public String get(String key) {
        String result = get0(asEnvKey(key));
        if (result == null) {
            result = get0(asPropertyKey(key));
        }
        return result;
    }

    private String get0(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = properties.getProperty(key);
            if (value == null && bundleName != null) {
                try {
                    value = ResourceBundle.getBundle(bundleName).getString(key);
                } catch (MissingResourceException ignore) {
                }
            }
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        String result = get(key);
        return result != null ? result : defaultValue;
    }

    private static String asEnvKey(String key) {
        return key.replace('.', '_').toUpperCase();
    }

    private static String asPropertyKey(String key) {
        return key.replace('_', '.').toLowerCase();
    }
}
