/*
 * Created on Nov 30, 2004
 *
 */
package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Luis Cruz
 *
 */
public class PropertiesManager {

    private static final Properties properties = new Properties();

    private static final PropertiesManager instance = new PropertiesManager();

    static {
        try {
            loadProperties(properties, "/configurationSQLInstructions.properties");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties file.", e);
        }
    }

    private PropertiesManager() {
    }

    protected static void loadProperties(final Properties properties, final String fileName) throws IOException {
        final InputStream inputStream = instance.getClass().getResourceAsStream(fileName);
        if (inputStream != null) {
            properties.load(inputStream);
        }
    }

    public static String getProperty(final String key) {
        return properties.getProperty(key);
    }

}