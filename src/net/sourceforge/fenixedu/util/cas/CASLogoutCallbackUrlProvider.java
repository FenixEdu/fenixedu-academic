package net.sourceforge.fenixedu.util.cas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.lang.StringUtils;

public class CASLogoutCallbackUrlProvider {
    private static final Map<String, String> logoutCallbackUrlsByHostnameMap = new HashMap<String, String>();

    static {
        final String propertiesFilename = "/.casLogoutCallbackUrlHostnames.properties";
        try {
            final Properties properties = new Properties();
            PropertiesManager.loadProperties(properties, propertiesFilename);
            for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                final Entry entry = (Entry) iterator.next();
                final String serviceUrlByHostnameKey = (String) entry.getKey();
                final String serviceUrl = (String) entry.getValue();
                final String hostname = serviceUrlByHostnameKey.substring("cas.logoutCallbackUrl.".length());

                logoutCallbackUrlsByHostnameMap.put(hostname, serviceUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLogoutCallbackUrl(final String requestURL) {
        for (final Entry<String, String> entry : logoutCallbackUrlsByHostnameMap.entrySet()) {
            final String hostname = entry.getKey();
            if (StringUtils.substringAfter(requestURL, "://").startsWith(hostname)) {
                return entry.getValue();
            }
        }

        return null;
    }

}
