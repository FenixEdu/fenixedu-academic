package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.MessageContext;

public class PaymentManagement implements IPaymentManagement {

	private static final String storedPassword;
	private static final String storedUsername;
	private static final String CGD = "CGD";
	private static final String CC = "CC";

	static {
		storedUsername = PropertiesManager.getProperty("webServices.PaymentManagement.username");
		storedPassword = PropertiesManager.getProperty("webServices.PaymentManagement.password");
	}

	@Override
	public String generatePaymentTicket(String username, String password, String source, MessageContext context)
			throws NotAuthorizedException {
		checkPermissions(username, password, context);
		Person person = null;
		if (source.startsWith(CGD) && source.length() == 18) {
			CardGenerationEntry card =
					CardGenerationEntry.readCardByCGDIdentifier(source.substring(CGD.length(), source.length() - 2));
			if (card != null) {
				person = card.getPerson();
			}
		}
		if (source.startsWith(CC)) {
			person = Person.readByDocumentIdNumberAndIdDocumentType(source.substring(CC.length()), IDDocumentType.IDENTITY_CARD);
		}
		return person == null ? StringUtils.EMPTY : person.generatePaymentTicket();
	}

	private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
		// check user/pass
		if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
			throw new NotAuthorizedException();
		}
	}

}
