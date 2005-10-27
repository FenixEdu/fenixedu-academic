package net.sourceforge.fenixedu.presentationTier.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class IndexHostRedirector {

    private static final Map<String, String> redirectPageMap = new HashMap<String, String>();

    static {
        final String propertiesFilename = "/.indexRedirectHostnames.properties";
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

    public static String getRedirectPage(final String requestURL) {
        for (final Entry<String, String> entry : redirectPageMap.entrySet()) {
            final String hostname = entry.getKey();
            if (StringUtils.substringAfter(requestURL, "://").startsWith(hostname)) {
                return entry.getValue();
            }
        }
        return "publico/index.html";
    }

}
