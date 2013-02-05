package net.sourceforge.fenixedu.webServices;

import java.util.UUID;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public class AlumniManagement implements IAlumniManagement {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.username");
        storedPassword = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.password");
    }

    @Override
    public String validateAlumniIdentity(String username, String password, String requestOID, String requestUUID,
            MessageContext context) throws NotAuthorizedException {

        checkPermissions(username, password, context);
        AlumniIdentityCheckRequest identityCheckRequest =
                (AlumniIdentityCheckRequest) DomainObject.fromOID(Long.valueOf(requestOID));
        if (identityCheckRequest != null && identityCheckRequest.getRequestToken().equals(UUID.fromString(requestUUID))) {
            return identityCheckRequest.getAlumni().getLoginUsername();
        }
        return null;
    }

    private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
        // check user/pass
        if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
            throw new NotAuthorizedException();
        }

        // check hosts accessing this service
        if (!HostAccessControl.isAllowed(this, (ServletRequest) context.getProperty("XFireServletController.httpServletRequest"))) {
            throw new NotAuthorizedException();
        }
    }

}
