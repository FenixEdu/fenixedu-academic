package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;

public class SetEmail extends Service {

    public class NotAuthorizedException extends FenixServiceException {
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

    public class UserDoesNotExistException extends FenixServiceException {

    }

    private static final Set<String> allowedHosts = new HashSet<String>();
    private static final String password;
    static {
	final String allowedHostString = PropertiesManager.getProperty("email.admin.allowed.hosts");
	if (allowedHostString != null) {
	    final String[] allowedHostTokens = allowedHostString.split(",");
	    for (int i = 0; i < allowedHostTokens.length; i++) {
		allowedHosts.add(allowedHostTokens[i]);
	    }
	}
	password = PropertiesManager.getProperty("email.admin.password");
    }

    public static boolean isAllowed(final String host, final String ip, final String password) {
	return SetEmail.password != null && SetEmail.password.equals(password) &&
		(allowedHosts.contains(host) || allowedHosts.contains(ip));
    }

    private void set(final String userUId, final String email) throws FenixServiceException {
	final User user = User.readUserByUserUId(userUId);
	if (user == null) {
	    throw new UserDoesNotExistException();
	}
	final Person person = user.getPerson();
	if (person == null) {
	    throw new UserDoesNotExistException();
	}
//	final String currentEmail = person.getInstitutionalEmail();
//	if (currentEmail == null || currentEmail.length() == 0) {
        final String newEmail = email != null && email.length() > 0 ? email : null; 
	person.setInstitutionalEmail(newEmail);
//	} else {
//	    throw new UserAlreadyHasEmailException(currentEmail);
//	}
    }

    public void run(final String host, final String ip, final String password, final String userUId, final String email)
    		throws FenixServiceException {
	if (isAllowed(host, ip, password)) {
	    set(userUId, email);
	} else {
	    throw new NotAuthorizedException();
	}
    }

}
