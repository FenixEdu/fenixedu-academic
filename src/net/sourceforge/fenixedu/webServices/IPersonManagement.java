package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationDTO;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IPersonManagement {

    public abstract PersonInformationDTO getPersonInformation(String username, String password, String unserUID,
	    MessageContext context) throws NotAuthorizedException;

}