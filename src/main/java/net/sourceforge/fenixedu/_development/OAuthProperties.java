/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu._development;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

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
