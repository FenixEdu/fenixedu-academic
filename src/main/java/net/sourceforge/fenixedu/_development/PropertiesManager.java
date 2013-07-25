package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * @author Luis Cruz
 * 
 */
public class PropertiesManager extends pt.utl.ist.fenix.tools.util.PropertiesManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);

    public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

    private static final Properties properties = new Properties();

    static {
        try {
            logger.debug("Loading Configuration Properties");
            final InputStream inputStream = PropertiesManager.class.getResourceAsStream("/configuration.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties files.", e);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static boolean isInDevelopmentMode() {
        return PropertiesManager.getBooleanProperty("development.mode");
    }

    public static boolean useBarraAsAuthenticationBroker() {
        return PropertiesManager.getBooleanProperty("barra.as.authentication.broker");
    }

    public static boolean hasGoogleAnalytics() {
        return !Strings.isNullOrEmpty(PropertiesManager.getProperty("google.analytics.snippet"));
    }

    public static String getProperty(final String key) {
        return properties.getProperty(key);
    }

    public static boolean getBooleanProperty(final String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static Integer getIntegerProperty(final String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

    public static void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
    }

}
