package net.sourceforge.fenixedu.presentationTier.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import pt.ist.bennu.core.util.ConfigurationManager;

import org.apache.commons.lang.StringUtils;

public class HostRedirector {

    private static final Map<String, String> redirectPageIndexMap = new HashMap<String, String>();
    private static final Map<String, String> redirectPageLoginMap = new HashMap<String, String>();
    private static final String indexRedirectPrefix = "index.html.link.";
    private static final String loginRedirectPrefix = "login.html.link.";

    static {
        constructRedirectMap(redirectPageIndexMap, indexRedirectPrefix);
        constructRedirectMap(redirectPageLoginMap, loginRedirectPrefix);
    }

    private static void constructRedirectMap(final Map<String, String> redirectPageMap, final String prefix) {
        final Properties properties = ConfigurationManager.getProperties();
        for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
            final Entry entry = (Entry) iterator.next();
            final String hostnameKey = (String) entry.getKey();
            if (hostnameKey.startsWith(prefix)) {
                final String redirectPage = (String) entry.getValue();
                final String hostname = StringUtils.remove(hostnameKey, prefix);
                System.out.println("Adding host redirect: " + hostname + " ---> " + redirectPage);
                redirectPageMap.put(hostname, redirectPage);
            }
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
            if (urlHostname.startsWith(hostname)
                    && (urlHostname.length() == hostnameLengrh || urlHostname.charAt(hostnameLengrh) == '.'
                            || urlHostname.charAt(hostnameLengrh) == ':' || urlHostname.charAt(hostnameLengrh) == '/')) {
                return entry.getValue();
            }
        }
        return "publico/index.html";
    }
}
