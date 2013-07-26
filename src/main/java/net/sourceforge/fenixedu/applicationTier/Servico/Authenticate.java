package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.FileUtils;
import edu.yale.its.tp.cas.client.CASReceipt;

/**
 * @author Luis Cruz
 * 
 */
public class Authenticate implements Serializable {

    private static final String URL_ENCODING = CharEncoding.UTF_8;

    private static final Logger logger = LoggerFactory.getLogger(Authenticate.class);

    protected static final Map allowedRolesByHostname = new HashMap();

    protected static final boolean validateExpirationDate;

    private static String buildVersion = null;

    private static String prefix = "filter.hostname.";

    static {
        validateExpirationDate = PropertiesManager.getBooleanProperty("validateExpirationDate");
        final Properties properties = PropertiesManager.getProperties();
        for (Object element : properties.entrySet()) {
            final Entry entry = (Entry) element;
            final String hostnameKey = (String) entry.getKey();
            final String rolesList = (String) entry.getValue();

            if (hostnameKey.startsWith(prefix)) {
                final String hostname = StringUtils.remove(hostnameKey, prefix);
                final String[] roles = rolesList.split(",");

                final Set rolesSet = new HashSet(roles.length);
                for (String role : roles) {
                    final RoleType roleType = RoleType.valueOf(role.trim());
                    if (LogLevel.INFO) {
                        logger.info("Host: " + hostname + " provides role: " + roleType.toString() + '.');
                    }
                    rolesSet.add(roleType);
                }
                allowedRolesByHostname.put(hostname, rolesSet);
            }
        }
        try {
            final InputStream inputStream = Authenticate.class.getResourceAsStream("/build.version");
            buildVersion = FileUtils.readFile(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load /.build.version. User authentication is therefor not possible.");
        }
    }

    protected User run(final String username, final String password, final String requestURL, final String remoteHost)
            throws ExcepcaoAutenticacao, FenixServiceException {

        Person person = Person.readPersonByUsernameWithOpenedLogin(username);
        if (person == null || !PasswordEncryptor.areEquals(person.getPassword(), password)) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }

        setLoginHostNameAndDateTime(remoteHost, person);

        return getUserView(person, requestURL);
    }

    public static class NonExistingUserException extends ExcepcaoAutenticacao {

        public NonExistingUserException(final String message) {
            super(message);
        }
    }

    protected User run(final CASReceipt receipt, final String requestURL, final String remoteHost) throws ExcepcaoAutenticacao,
            ExcepcaoPersistencia {
        final String username = receipt.getUserName();

        Person person = Person.readPersonByUsernameWithOpenedLogin(username);
        if (person == null) {
            System.out.println("Attempted login of non-existent user: " + username);
            throw new NonExistingUserException("error.Exception");
        }

        setLoginHostNameAndDateTime(remoteHost, person);

        if (validateExpirationDate) {
            try {
                final DateTime expirationDate = Script.returnExpirationDate(person.getIstUsername());
                return getUserView(person, requestURL, expirationDate);
            } catch (KerberosException e) {
                return getUserView(person, requestURL);
            }
        } else {
            return getUserView(person, requestURL);
        }
    }

    private void setLoginHostNameAndDateTime(final String remoteHost, Person person) {
        // final User user = person.getUser();
        // RegisterUserLoginThread.runThread(user, remoteHost);
    }

    // Service Invokers migrated from Berserk

    private static final Authenticate serviceInstance = new Authenticate();

    @Atomic
    public static User runAuthenticate(String username, String password, String requestURL, String remoteHost)
            throws ExcepcaoAutenticacao, FenixServiceException {
        return serviceInstance.run(username, password, requestURL, remoteHost);
    }

    @Atomic
    public static User runAuthenticate(final CASReceipt receipt, final String requestURL, final String remoteHost)
            throws ExcepcaoAutenticacao, FenixServiceException, ExcepcaoPersistencia {
        return serviceInstance.run(receipt, requestURL, remoteHost);
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static User runLocalAuthenticate(String username, String password, String requestURL, String remoteHost)
            throws ExcepcaoAutenticacao, FenixServiceException {
        return serviceInstance.run(username, password, requestURL, remoteHost);
    }

}