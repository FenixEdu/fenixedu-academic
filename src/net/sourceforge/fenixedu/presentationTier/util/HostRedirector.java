package net.sourceforge.fenixedu.presentationTier.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.lang.StringUtils;

public class HostRedirector {

    private static final Map<String, String> redirectPageIndexMap = new HashMap<String, String>();
    private static final Map<String, String> redirectPageLoginMap = new HashMap<String, String>();

    static {
        constructRedirectMap("/.indexRedirectHostnames.properties", redirectPageIndexMap, 16);
        constructRedirectMap("/.loginRedirectHostnames.properties", redirectPageLoginMap, 16);
    }

    private static void constructRedirectMap(final String propertiesFilename, final Map<String, String> redirectPageMap, final int propertyOffSet) {
        try {
            final Properties properties = new Properties();
            PropertiesManager.loadProperties(properties, propertiesFilename);
            for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                final Entry entry = (Entry) iterator.next();
                final String hostnameKey = (String) entry.getKey();
                final String redirectPage = (String) entry.getValue();
                final String hostname = hostnameKey.substring(16);
                System.out.println("Adding host redirect: " + hostname + " ---> " + redirectPage);
                redirectPageMap.put(hostname, redirectPage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load " + propertiesFilename
                    + ". Index page redirection is therefor not possible.");
        }
    }

    public static String getRedirectPageIndex(final String requestURL) {
        return getRedirectPageIndex(redirectPageIndexMap, requestURL);
    }

    public static String getRedirectPageLogin(final String requestURL) {
        return getRedirectPageIndex(redirectPageLoginMap, requestURL);
    }

    private static String getRedirectPageIndex(final Map<String, String> redirectPageMap, final String requestURL) {
        for (final Entry<String, String> entry : redirectPageMap.entrySet()) {
            final String hostname = entry.getKey();
            final String urlHostname = StringUtils.substringAfter(requestURL, "://");
            final int hostnameLengrh = hostname.length();
            if (urlHostname.startsWith(hostname) &&
                    (urlHostname.length() == hostnameLengrh
                            || urlHostname.charAt(hostnameLengrh) == '.'
                            || urlHostname.charAt(hostnameLengrh) == ':'
                            || urlHostname.charAt(hostnameLengrh) == '/')) {
                return entry.getValue();
            }
        }
        return "publico/index.html";
    }
}
