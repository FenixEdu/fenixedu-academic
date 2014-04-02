package net.sourceforge.fenixedu;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class FenixIstConfiguration {
    @ConfigurationManager(description = "Fenix IST specific properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "db.giaf.user", defaultValue = "root")
        public String dbGiafUser();

        @ConfigurationProperty(key = "db.giaf.pass")
        public String dbGiafPass();

        @ConfigurationProperty(key = "db.giaf.alias", defaultValue = "//localhost:3306/AssiduidadeOracleTeste")
        public String dbGiafAlias();

        @ConfigurationProperty(key = "ldap.sync.services.username")
        public String ldapSyncServicesUsername();

        @ConfigurationProperty(key = "ldap.sync.services.password")
        public String ldapSyncServicesPassword();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
