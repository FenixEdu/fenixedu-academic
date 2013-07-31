/**
 * 
 */
package net.sourceforge.fenixedu.webServices;

import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public class PersonInformation implements IPersonInformation {

    private static final String storedPassword;

    private static final String storedUsername;

    static {
        storedUsername = ConfigurationManager.getProperty("webServices.PersonInformation.getPersonInformation.username");
        storedPassword = ConfigurationManager.getProperty("webServices.PersonInformation.getPersonInformation.password");
    }

    @Override
    public PersonInformationBean getPersonInformation(String username, String password, String istUserName, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);
        User foundUser = User.readUserByUserUId(istUserName);
        return foundUser == null ? null : new PersonInformationBean(foundUser.getPerson());
    }

    private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
        // check user/pass
        if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
            throw new NotAuthorizedException();
        }

        // check hosts accessing this service
        //	if (!HostAccessControl.isAllowed(this, (ServletRequest) context.getProperty("XFireServletController.httpServletRequest"))) {
        //	    throw new NotAuthorizedException();
        //	}
    }

}