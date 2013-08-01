package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.fenixframework.Atomic;

public class SetEmail {

    public static class NotAuthorizedException extends FenixServiceException {
    }

    public class UserAlreadyHasEmailException extends FenixServiceException {

        private final String email;

        public UserAlreadyHasEmailException(final String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

    }

    public static class UserDoesNotExistException extends FenixServiceException {

    }

    private static final Set<String> allowedHosts = new HashSet<String>();
    private static final String password;
    static {
        final String allowedHostString = ConfigurationManager.getProperty("email.admin.allowed.hosts");
        if (allowedHostString != null) {
            final String[] allowedHostTokens = allowedHostString.split(",");
            for (String allowedHostToken : allowedHostTokens) {
                allowedHosts.add(allowedHostToken);
            }
        }
        password = ConfigurationManager.getProperty("email.admin.password");
    }

    public static boolean isAllowed(final String host, final String ip, final String password) {
        return SetEmail.password != null && SetEmail.password.equals(password)
                && (allowedHosts.contains(host) || allowedHosts.contains(ip));
    }

    private static void set(final String userUId, final String email) throws FenixServiceException {
        final User user = Login.readUserByUserUId(userUId);
        if (user == null) {
            throw new UserDoesNotExistException();
        }
        final Person person = user.getPerson();
        if (person == null) {
            throw new UserDoesNotExistException();
        }
        final String newEmail = StringUtils.isEmpty(email) ? null : email;
        person.setInstitutionalEmailAddressValue(newEmail);
    }

    @Atomic
    public static void run(final String host, final String ip, final String password, final String userUId, final String email)
            throws FenixServiceException {
        if (isAllowed(host, ip, password)) {
            set(userUId, email);
        } else {
            throw new NotAuthorizedException();
        }
    }

}