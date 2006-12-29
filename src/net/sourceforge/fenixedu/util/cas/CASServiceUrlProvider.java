package net.sourceforge.fenixedu.util.cas;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.lang.StringUtils;

public class CASServiceUrlProvider {
    private static final Map<String, String> serviceUrlsByHostnameMap = new HashMap<String, String>();

    static {
        final String propertiesFilename = "/.casServiceUrlHostnames.properties";
        try {
            final Properties properties = new Properties();
            PropertiesManager.loadProperties(properties, propertiesFilename);
            for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                final Entry entry = (Entry) iterator.next();
                final String serviceUrlByHostnameKey = (String) entry.getKey();
                final String serviceUrl = (String) entry.getValue();
                final String hostname = serviceUrlByHostnameKey.substring("cas.serviceUrl.".length());

                serviceUrlsByHostnameMap.put(hostname, serviceUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //fenix fenix.linkare.local
    public static String getServiceUrl(final String requestURL) {
    	URL location;
		try {
			location = new URL(requestURL);
		} catch (MalformedURLException e) {
			return null;
		}
    	for (final Entry<String, String> entry : serviceUrlsByHostnameMap.entrySet()) {
            final String hostname = entry.getKey();
            if (location.getHost().startsWith(hostname)) {
                return entry.getValue();
            }
        }

        return null;
    }

}
