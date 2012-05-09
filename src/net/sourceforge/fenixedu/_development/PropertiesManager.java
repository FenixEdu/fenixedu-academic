/*
 * Created on Nov 30, 2004
 *
 */
package net.sourceforge.fenixedu._development;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.Config;
import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixframework.FenixFrameworkPlugin;
import pt.ist.fenixframework.pstm.dml.FenixDomainModelWithOCC;

/**
 * @author Luis Cruz
 * 
 */
public class PropertiesManager extends pt.utl.ist.fenix.tools.util.PropertiesManager {
    
    public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

    private static final Properties properties = new Properties();

    static {
	try {
	    loadProperties(properties, "/configuration.properties");
	    loadProperties(properties, "/.build.properties");
	} catch (IOException e) {
	    throw new RuntimeException("Unable to load properties files.", e);
	}
    }

    public static boolean isInDevelopmentMode() {
	return PropertiesManager.getBooleanProperty("development.mode");
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
	final Map<String, CasConfig> casConfigMap = new HashMap<String, CasConfig>();
	for (final Object key : properties.keySet()) {
	    final String property = (String) key;
	    int i = property.indexOf(".cas.enable");
	    if (i >= 0) {
		final String hostname = property.substring(0, i);
		if (getBooleanProperty(property)) {
		    final String casLoginUrl = getProperty(hostname + ".cas.loginUrl");
		    final String casLogoutUrl = getProperty(hostname + ".cas.logoutUrl");
		    final String casValidateUrl = getProperty(hostname + ".cas.ValidateUrl");
		    final String serviceUrl = getProperty(hostname + ".cas.serviceUrl");

		    final CasConfig casConfig = new CasConfig(casLoginUrl, casLogoutUrl, casValidateUrl, serviceUrl);
		    casConfigMap.put(hostname, casConfig);
		}
	    }
	}

	return new Config() {
	    {
		domainModelClass = FenixDomainModelWithOCC.class;
		domainModelPath = domainModel;
		dbAlias = getProperty("db.alias");
		dbUsername = getProperty("db.user");
		dbPassword = getProperty("db.pass");
		appName = getProperty("app.name");
		rootClass = RootDomainObject.class;
		collectDataAccessPatterns = false;
		appContext = getProperty("app.context");
		filterRequestWithDigest = getBooleanProperty("filter.request.with.digest");
		tamperingRedirect = getProperty("login.page");
		errorIfChangingDeletedObject = getBooleanProperty("error.if.changing.deleted.object");
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

		plugins = getPluginArray();
	    }

	    private FenixFrameworkPlugin[] getPluginArray() {
		String property = getProperty("plugins");
		if (StringUtils.isEmpty(property)) {
		    return new FenixFrameworkPlugin[0];
		}
		String[] classNames = property.split("\\s*,\\s*");

		FenixFrameworkPlugin[] pluginArray = new FenixFrameworkPlugin[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
		    try {
			pluginArray[i] = (FenixFrameworkPlugin) Class.forName(classNames[i].trim()).newInstance();
		    } catch (InstantiationException e) {
			throw new Error(e);
		    } catch (IllegalAccessException e) {
			throw new Error(e);
		    } catch (ClassNotFoundException e) {
			throw new Error(e);
		    }
		}
		return pluginArray;
	    }

	};
    }

}
