package net.sourceforge.fenixedu.webServices;

import javax.servlet.ServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.MessageContext;

public class LibraryManagement implements ILibraryManagement {

    private static final String storedPassword;
    private static final String storedUsername;
    private static final String CGD = "CGD";
    private static final String CC = "CC";

    static {
	storedUsername = PropertiesManager.getProperty("webServices.LibraryManagement.username");
	storedPassword = PropertiesManager.getProperty("webServices.LibraryManagement.password");
    }

    @Override
    public String convertLibraryNumbers(String username, String password, MessageContext context, String source)
	    throws NotAuthorizedException {
	checkPermissions(username, password, context);
	Person person = null;
	if (source.startsWith(CGD)) {
	    CardGenerationEntry card = CardGenerationEntry.readCardByCGDIdentifier(source.substring(CGD.length()));
	    if (card != null) {
		person = card.getPerson();
	    }
	}
	if (source.startsWith(CC)) {
	    person = Person.readByDocumentIdNumberAndIdDocumentType(source.substring(CC.length()), IDDocumentType.IDENTITY_CARD);
	}
	if (person != null) {
	    String libraryCard = person.getLibraryCard().getCardNumber();
	    if (libraryCard != null) {
		return libraryCard;
	    }
	    return person.getIstUsername();
	}
	return StringUtils.EMPTY;
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
