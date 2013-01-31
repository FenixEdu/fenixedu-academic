/**
 * 
 */
package net.sourceforge.fenixedu.webServices;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.person.UpdatePersonInformationFromCitizenCard;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationDTO;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationFromUniqueCardDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonManagement implements IPersonManagement {

	private static final String storedPassword;

	private static final String storedUsername;

	static {
		storedUsername = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.username");
		storedPassword = PropertiesManager.getProperty("webServices.PersonManagement.getPersonInformation.password");
	}

	@Override
	public PersonInformationDTO getPersonInformation(String username, String password, String unserUID, MessageContext context)
			throws NotAuthorizedException {
		checkPermissions(username, password, context);
		User foundUser = User.readUserByUserUId(unserUID);
		return foundUser == null ? null : new PersonInformationDTO(foundUser.getPerson());
	}

	@Override
	public Boolean setPersonInformation(String username, String password, PersonInformationFromUniqueCardDTO personDTO,
			MessageContext context) throws NotAuthorizedException {

		checkPermissions(username, password, context);

		personDTO.print();

		try {
			UpdatePersonInformationFromCitizenCard.run(personDTO);
		} catch (DomainException e) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
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