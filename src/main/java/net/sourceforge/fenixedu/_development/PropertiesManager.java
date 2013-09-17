package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.Config;
import pt.ist.fenixWebFramework.Config.CasConfig;

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

    public static Config getFenixWebFrameworkConfig() {
        logger.debug("Loading FenixWebFramework Configuration");
        final Map<String, CasConfig> casConfigMap = new HashMap<String, CasConfig>();
        for (final Object key : properties.keySet()) {
            final String property = (String) key;
            int i = property.indexOf(".cas.enable");
            if (i >= 0) {
                final String hostname = property.substring(0, i);
                if (getBooleanProperty(property)) {
                    final String casLoginUrl = getProperty(hostname + ".cas.loginUrl");
                    logger.info("Setting CAS Login URL: " + casLoginUrl);
                    final String casLogoutUrl = getProperty(hostname + ".cas.logoutUrl");
                    logger.info("Setting CAS Logout URL: " + casLogoutUrl);
                    final String casValidateUrl = getProperty(hostname + ".cas.ValidateUrl");
                    logger.info("Setting CAS Validate URL: " + casValidateUrl);
                    final String serviceUrl = getProperty(hostname + ".cas.serviceUrl");
                    logger.info("Setting CAS Service URL: " + serviceUrl);
                    final CasConfig casConfig = new CasConfig(casLoginUrl, casLogoutUrl, casValidateUrl, serviceUrl);
                    casConfigMap.put(hostname, casConfig);
                }
            }
        }

        return new Config() {
            {
                appContext = getProperty("app.context");
                filterRequestWithDigest = getBooleanProperty("filter.request.with.digest");
                tamperingRedirect = getProperty("login.page");
                defaultLanguage = getProperty("language");
                defaultLocation = getProperty("location");
                defaultVariant = getProperty("variant");
                logProfileDir = getProperty("log.profile.dir");
                logProfileFilename = getProperty("log.profile.filename");

                dspaceClientTransportClass = getProperty("dspace.client.transport.class");
                fileManagerFactoryImplementationClass = getProperty("file.manager.factory.implementation.class");
                dspaceServerUrl = getProperty("dspace.serverUrl");
                dspaceDownloadUriFormat = getProperty("dspace.downloadUriFormat");
                dspaceUsername = getProperty("dspace.username");
                dspacePassword = getProperty("dspace.password");
                dspaceRmiServerName = getProperty("dspace.rmi.server.name");
                jndiPropertiesFile = getProperty("jndi.properties.file");
                rmiRegistryPort = getProperty("rmi.registry.port");
                rmiPort = getProperty("rmi.port");
                rmiSsl = getProperty("rmi.ssl");
                rmiSslTruststore = getProperty("rmi.ssl.truststore");
                rmiSslTruststorePassword = getProperty("rmi.ssl.truststore.password");
                rmiStreamBytesMin = getProperty("rmi.stream.bytes.min");
                rmiStreamBytesMax = getProperty("rmi.stream.bytes.max");
                rmiStreamBytesBlock = getProperty("rmi.stream.bytes.block");

                casConfigByHost = Collections.unmodifiableMap(casConfigMap);

                exceptionHandlerClassname = "net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler";

            }

        };
    }

}
