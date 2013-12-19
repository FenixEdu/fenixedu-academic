package net.sourceforge.fenixedu.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class HostRedirector {

    private Map<String, String> redirectPageIndexMap;
    private Map<String, String> redirectPageLoginMap;

    HostRedirector(Map<String, String> redirectPageIndexMap, Map<String, String> redirectPageLoginMap) {
        this.redirectPageIndexMap = redirectPageIndexMap;
        this.redirectPageLoginMap = redirectPageLoginMap;
    }

    public String getRedirectPageIndex(final String requestURL) {
        return getRedirectPageIndex(redirectPageIndexMap, requestURL);
    }

    public String getRedirectPageLogin(final String requestURL) {
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
