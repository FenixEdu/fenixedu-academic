package net.sourceforge.fenixedu.webServices;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public class LibraryManagement implements ILibraryManagement {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
	storedUsername = PropertiesManager.getProperty("webServices.LibraryManagement.getPersonInformation.username");
	storedPassword = PropertiesManager.getProperty("webServices.LibraryManagement.getPersonInformation.password");
    }

    @Override
    public String getLibraryCardNumberByIstUsername(String username, String password, MessageContext context, String istUsername)
	    throws NotAuthorizedException {

	checkPermissions(username, password, context);
	String libraryCardNumber = "";
	User user = User.readUserByUserUId(istUsername);

	if (user != null && user.getPerson() != null) {
	    LibraryCard card = user.getPerson().getLibraryCard();
	    if (card != null) {
		libraryCardNumber = card.getCardNumber();
	    }
	}

	return libraryCardNumber;
    }

    @Override
    public String getLibraryCardNumberByCitizenCardNumber(String username, String password, MessageContext context,
	    String citizenCardNumber) throws NotAuthorizedException {

	checkPermissions(username, password, context);
	String libraryCardNumber = "";

	Person person = Person.readByDocumentIdNumberAndIdDocumentType(citizenCardNumber, IDDocumentType.IDENTITY_CARD);

	if (person != null) {
	    LibraryCard card = person.getLibraryCard();
	    if (card != null) {
		libraryCardNumber = card.getCardNumber();
	    }
	}

	return libraryCardNumber;
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
