package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IPersonInformation {

    public abstract PersonInformationBean getPersonInformation(String username, String password, String unserUID,
	    MessageContext context) throws NotAuthorizedException;
}
