package net.sourceforge.fenixedu._development;

import org.apache.commons.lang.StringUtils;

public class OAuthProperties {

    private final static String CODE_EXPIRATION_SECONDS_DEFAULT = "60";
    private final static String CODE_EXPIRATION_SECONDS_KEY = "fenix.api.oauth.code.timeout.seconds";
    private final static String ACCESS_TOKEN_EXPIRATION_SECONDS_DEFAULT = "3600";
    private final static String ACCESS_TOKEN_EXPIRATION_KEY = "fenix.api.oauth.access.token.timeout.seconds";
    private final static String FENIX_API_NEWS_RSS_URL_KEY = "fenix.api.news.rss.url";
    private final static String FENIX_API_NEWS_RSS_URL_DEFAULT = "http://www.ist.utl.pt/pt/noticias/rss";
    private final static String FENIX_API_EVENTS_RSS_URL_KEY = "fenix.api.events.rss.url";
    private final static String FENIX_API_EVENTS_RSS_URL_DEFAULT = "http://www.ist.utl.pt/pt/eventos/rss";

    public static Integer getCodeExpirationSeconds() {
        return Integer.valueOf(getProperty(CODE_EXPIRATION_SECONDS_KEY, CODE_EXPIRATION_SECONDS_DEFAULT));
    }

    public static Integer getAccessTokenExpirationSeconds() {
        return Integer.valueOf(getProperty(ACCESS_TOKEN_EXPIRATION_KEY, ACCESS_TOKEN_EXPIRATION_SECONDS_DEFAULT));
    }

    public static String getFenixApiNewsRssUrl() {
        return getProperty(FENIX_API_NEWS_RSS_URL_KEY, FENIX_API_NEWS_RSS_URL_DEFAULT);
    }

    public static String getFenixApiEventsRssUrl() {
        return getProperty(FENIX_API_EVENTS_RSS_URL_KEY, FENIX_API_EVENTS_RSS_URL_DEFAULT);
    }

    private static String getProperty(String key, String defaultValue) {
        String stringProperty = PropertiesManager.getProperty(key);

        if (StringUtils.isBlank(stringProperty)) {
            return defaultValue;
        }

        return stringProperty;
    }

}
