package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationDTO;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationFromUniqueCardDTO;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IPersonManagement {

	public abstract PersonInformationDTO getPersonInformation(String username, String password, String unserUID,
			MessageContext context) throws NotAuthorizedException;

	public abstract Boolean setPersonInformation(String username, String password, PersonInformationFromUniqueCardDTO personDTO,
			MessageContext context) throws NotAuthorizedException;
}