/*
 * Created on Nov 30, 2004
 *
 */
package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.util.Properties;

import pt.ist.fenixWebFramework.Config;

/**
 * @author Luis Cruz
 *
 */
public class PropertiesManager extends pt.utl.ist.fenix.tools.util.PropertiesManager {

    private static final Properties properties = new Properties();

    static {
        try {
            loadProperties(properties, "/configuration.properties");
            loadProperties(properties, "/.build.properties");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties files.", e);
        }
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

    public static Config getFenixFrameworkConfig(final String domainModel) {
        return new Config() {{
            domainModelPath = domainModel;
            dbAlias = getProperty("db.alias");
            dbUsername = getProperty("db.user");
            dbPassword = getProperty("db.pass");
            appName = getProperty("app.name");
            errorIfChangingDeletedObject = getBooleanProperty("error.if.changing.deleted.object");
            defaultLanguage = getProperty("language");
            defaultLocation = getProperty("location");
            defaultVariant = getProperty("variant");
            logProfileDir = getProperty("log.profile.dir");
            logProfileFilename = getProperty("log.profile.filename");
        }};
    }
}
