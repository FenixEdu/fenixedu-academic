/*
 * Created on Nov 30, 2004
 *
 */
package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.Config;
import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixframework.FenixFrameworkPlugin;
import pt.ist.fenixframework.artifact.FenixFrameworkArtifact;
import pt.ist.fenixframework.project.DmlFile;
import pt.ist.fenixframework.project.exception.FenixFrameworkProjectException;
import pt.ist.fenixframework.pstm.dml.FenixDomainModelWithOCC;

import com.google.common.base.Strings;

/**
 * @author Luis Cruz
 * 
 */
public class PropertiesManager extends pt.utl.ist.fenix.tools.util.PropertiesManager {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesManager.class);

    public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

    private static Config config = null;

    private static final Properties properties = new Properties();

    private static List<URL> urls = null;

    static {
        try {
            LOG.debug("Loading Configuration Properties");
            loadProperties(properties, "/configuration.properties");
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

    public static Config getFenixFrameworkConfig() {
        LOG.debug("Loading FenixFramework Configuration");
        final Map<String, CasConfig> casConfigMap = new HashMap<String, CasConfig>();
        for (final Object key : properties.keySet()) {
            final String property = (String) key;
            int i = property.indexOf(".cas.enable");
            if (i >= 0) {
                final String hostname = property.substring(0, i);
                if (getBooleanProperty(property)) {
                    final String casLoginUrl = getProperty(hostname + ".cas.loginUrl");
                    LOG.info("Setting CAS Login URL: " + casLoginUrl);
                    final String casLogoutUrl = getProperty(hostname + ".cas.logoutUrl");
                    LOG.info("Setting CAS Logout URL: " + casLogoutUrl);
                    final String casValidateUrl = getProperty(hostname + ".cas.ValidateUrl");
                    LOG.info("Setting CAS Validate URL: " + casValidateUrl);
                    final String serviceUrl = getProperty(hostname + ".cas.serviceUrl");
                    LOG.info("Setting CAS Service URL: " + serviceUrl);
                    final CasConfig casConfig = new CasConfig(casLoginUrl, casLogoutUrl, casValidateUrl, serviceUrl);
                    casConfigMap.put(hostname, casConfig);
                }
            }
        }

        Config config = new Config() {
            {

                domainModelClass = FenixDomainModelWithOCC.class;
                domainModelPaths = new String[0];
                dbAlias = getProperty("db.alias");
                dbUsername = getProperty("db.user");
                dbPassword = getProperty("db.pass");
                appName = getProperty("app.name");
                rootClass = RootDomainObject.class;
                collectDataAccessPatterns = false;
                appContext = getProperty("app.context");
                filterRequestWithDigest = getBooleanProperty("filter.request.with.digest");
                tamperingRedirect = getProperty("login.page");
                errorIfChangingDeletedObject = true;
                errorfIfDeletingObjectNotDisconnected = true;
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

                plugins = new FenixFrameworkPlugin[0];
            }

            @Override
            public List<URL> getDomainModelURLs() {
                return getUrls();
            }

        };
        return config;
    }

    public static List<URL> getUrls() {
        if (urls == null) {
            urls = new ArrayList<URL>();
            try {
                for (DmlFile dml : FenixFrameworkArtifact.fromName(getProperty("app.name")).getFullDmlSortedList()) {
                    urls.add(dml.getUrl());
                }
            } catch (FenixFrameworkProjectException e) {
                throw new Error(e);
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        if (urls.isEmpty()) {
            System.err.println("domain Model URLs were not loaded");
        }
        return urls;
    }

}
