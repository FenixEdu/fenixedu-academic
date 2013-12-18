/**
 * 
 */
package net.sourceforge.fenixedu.webServices;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.person.UpdatePersonInformationFromCitizenCard;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationDTO;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationFromUniqueCardDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;
import org.fenixedu.bennu.core.domain.User;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonManagement implements IPersonManagement {

    private static final String storedPassword;

    private static final String storedUsername;

    static {
        storedUsername = FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementGetPersonInformationUsername();
        storedPassword = FenixConfigurationManager.getConfiguration().getWebServicesPersonManagementGetPersonInformationPassword();
    }

    @Override
    public PersonInformationDTO getPersonInformation(String username, String password, String unserUID, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);
        User foundUser = User.findByUsername(unserUID);
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
        if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this,
                (ServletRequest) context.getProperty("XFireServletController.httpServletRequest"))) {
            throw new NotAuthorizedException();
        }
    }

}