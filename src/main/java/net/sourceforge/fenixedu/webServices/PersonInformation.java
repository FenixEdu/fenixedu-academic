/**
 * 
 */
package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

import pt.ist.bennu.core.domain.User;

public class PersonInformation implements IPersonInformation {

    private static final String storedPassword;

    private static final String storedUsername;

    static {
        storedUsername =
                FenixConfigurationManager.getConfiguration().getWebServicesPersonInformationGetPersonInformationUsername();
        storedPassword =
                FenixConfigurationManager.getConfiguration().getWebServicesPersonInformationGetPersonInformationPassword();
    }

    @Override
    public PersonInformationBean getPersonInformation(String username, String password, String istUserName, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);
        User foundUser = User.findByUsername(istUserName);
        return foundUser == null ? null : new PersonInformationBean(foundUser.getPerson());
    }

    private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
        // check user/pass
        if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
            throw new NotAuthorizedException();
        }

        // check hosts accessing this service
        //	if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this, (ServletRequest) context.getProperty("XFireServletController.httpServletRequest"))) {
        //	    throw new NotAuthorizedException();
        //	}
    }

}