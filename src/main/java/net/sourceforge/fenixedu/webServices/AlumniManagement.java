package net.sourceforge.fenixedu.webServices;

import java.util.UUID;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

import pt.ist.fenixframework.FenixFramework;

public class AlumniManagement implements IAlumniManagement {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername = FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementgetPersonInformationUsername();
        storedPassword = FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementgetPersonInformationPassword();
    }

    @Override
    public String validateAlumniIdentity(String username, String password, String requestOID, String requestUUID,
            MessageContext context) throws NotAuthorizedException {

        checkPermissions(username, password, context);
        AlumniIdentityCheckRequest identityCheckRequest = FenixFramework.getDomainObject(requestOID);
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
        if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this,
                (ServletRequest) context.getProperty("XFireServletController.httpServletRequest"))) {
            throw new NotAuthorizedException();
        }
    }

}
