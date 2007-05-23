package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.User;

public class ConsultRoles extends Service {

    public class NotAuthorizedException extends FenixServiceException {
    }

    private static final Set<String> allowedHosts = new HashSet<String>();
    private static final String password;
    static {
	final String allowedHostString = PropertiesManager.getProperty("consult.roles.admin.allowed.hosts");
	if (allowedHostString != null) {
	    final String[] allowedHostTokens = allowedHostString.split(",");
	    for (int i = 0; i < allowedHostTokens.length; i++) {
		allowedHosts.add(allowedHostTokens[i]);
	    }
	}
	password = PropertiesManager.getProperty("consult.roles.admin.password");
    }

    public static boolean isAllowed(final String host, final String ip, final String password) {
	return ConsultRoles.password != null && ConsultRoles.password.equals(password) &&
		(allowedHosts.contains(host) || allowedHosts.contains(ip));
    }

    public Set<Role> run(final String host, final String ip, final String password, final String userUId)
    		throws FenixServiceException {
	if (isAllowed(host, ip, password)) {
	    final Login login = Login.readLoginByUsername(userUId);
	    final User user = login.getUser();
	    final Person person = user.getPerson();
	    return user == null ? null : person.getPersonRolesSet();
	} else {
	    throw new NotAuthorizedException();
	}
    }

}
