package net.sourceforge.fenixedu;

import org.fenixedu.bennu.core.annotation.ConfigurationManager;
import org.fenixedu.bennu.core.annotation.ConfigurationProperty;
import org.fenixedu.bennu.core.util.ConfigurationInvocationHandler;

public class FenixIstConfiguration {
    @ConfigurationManager(description = "Fenix IST specific properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "db.giaf.user")
        public String dbGiafUser();

        @ConfigurationProperty(key = "db.giaf.pass")
        public String dbGiafPass();

        @ConfigurationProperty(key = "db.giaf.alias")
        public String dbGiafAlias();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
