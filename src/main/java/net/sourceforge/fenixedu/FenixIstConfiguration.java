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

        @ConfigurationProperty(key = "sotisURL", defaultValue = "https://sotis.tecnico.ulisboa.pt")
        public String sotisURL();

        @ConfigurationProperty(key = "legacyFilesRedirectMapLocation", defaultValue = "")
        public String legacyFilesRedirectMapLocation();

        @ConfigurationProperty(key = "barra.as.authentication.broker",
                description = "CAS ticket validation through barra: https://fenix-ashes.ist.utl.pt/fenixWiki/Barra",
                defaultValue = "false")
        public Boolean barraAsAuthenticationBroker();

        @ConfigurationProperty(key = "barra.loginUrl",
                description = "Login URL to use when barra is set as authentication broker")
        public String barraLoginUrl();

    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    private static boolean barraLogin = getConfiguration().barraAsAuthenticationBroker();

    public static boolean barraLogin() {
        return barraLogin;
    }

    public static void setBarraLogin(boolean state) {
        barraLogin = state;
    }

}
