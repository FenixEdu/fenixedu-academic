package net.sourceforge.fenixedu._development;

import org.fenixedu.bennu.core.annotation.ConfigurationManager;
import org.fenixedu.bennu.core.annotation.ConfigurationProperty;
import org.fenixedu.bennu.core.util.ConfigurationInvocationHandler;

public class OAuthProperties {
    @ConfigurationManager(description = "OAuth Properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "fenix.api.oauth.code.timeout.seconds", defaultValue = "60")
        public Integer getCodeExpirationSeconds();

        @ConfigurationProperty(key = "fenix.api.oauth.access.token.timeout.seconds", defaultValue = "21600")
        public Integer getAccessTokenExpirationSeconds();

        @ConfigurationProperty(key = "fenix.api.news.rss.url")
        public String getFenixApiNewsRssUrl();

        @ConfigurationProperty(key = "fenix.api.events.rss.url")
        public String getFenixApiEventsRssUrl();

        @ConfigurationProperty(key = "fenix.api.allow.ist.ids", description = "allow managers to invoke api using _istid_ param",
                defaultValue = "false")
        public Boolean getFenixApiAllowIstIds();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
