/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.RecipientType;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:48:52,7/Fev/2006
 * @version $Id$
 */
public class EMailSender {

	private Collection<Recipient> recipients = new ArrayList<Recipient>();

	private EMailMessage message;

	private IGroup[] allowedSenders;

	public class SenderNotAllowed extends Exception {

	}

	private static String bundleFile = new String("SMTPConfiguration");

	private static ResourceBundle bundle = null;

	private static String mailServer = null;

	static {
		if (bundle == null) {
			try {
				EMailSender.bundle = ResourceBundle.getBundle(EMailSender.bundleFile);
				EMailSender.mailServer = EMailSender.bundle.getString("mailSender.server.url");
				if (EMailSender.mailServer == null) {
					EMailSender.mailServer = "mail.adm";
				}
			}
			catch (Exception e) {
				// the default server
				EMailSender.mailServer = "mail.adm";
			}
		}
	}

	public EMailSender(IGroup[] allowedSenders) {
		this.allowedSenders = allowedSenders;
	}

	public void setMessage(EMailMessage message) {
		this.message = message;
	}

	public void addRecipient(RecipientType type, Person... persons) {
		for (Person person : persons) {
			Recipient recipient = new Recipient();
			recipient.setType(type);
			recipient.setSubject(person);
			this.recipients.add(recipient);
		}
	}

	public void addRecipient(RecipientType type, Collection<Recipient> recipients) {
		this.recipients.addAll(recipients);
	}

	public void addRecipient(RecipientType type, List<IGroup> list) {
		IGroup[] groups = new IGroup[list.size()];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = list.get(i);
		}
		this.addRecipient(type, groups);
	}

	public void addRecipient(RecipientType type, IGroup... groups) {

		int i = 0;
		for (IGroup group : groups) {
			for (Person person : group.getElements()) {
				this.addRecipient(type, person);
			}
		}		
	}

	public SendMailReport send(EMailAddress from) throws SenderNotAllowed {
		if (!checkIfSenderIsAllowed()) {
			throw new SenderNotAllowed();
		}

		return this.sendToAll(message, from);

	}

	private boolean checkIfSenderIsAllowed() {
		boolean allowed = false;
		if (this.allowedSenders != null) {
			IUserView loggedUser = AccessControl.getUserView();
			for (int i = 0; i < this.allowedSenders.length; i++) {
				IGroup group = this.allowedSenders[i];
				if (group.allows(loggedUser)) {
					allowed = true;
					break;
				}
			}
		}
		return true; // TODO: this is here only for testing sending mails to
		// all degree curricular plan students
	}

	private SendMailReport sendToAll(EMailMessage message, EMailAddress from) {

		SendMailReport report = new SendMailReport();
		Collection<Person> bccList = new ArrayList<Person>();

		for (Recipient recipient : this.recipients) {
			if (!EMailAddress.isValid(recipient.getSubject().getEmail())) {
				report.add(SendStatus.INVALID_ADDRESS, recipient.getSubject());
			}
			else if (!EMailAddress.isValid(recipient.getSubject().getNome(), recipient.getSubject().getEmail())) {
				report.add(SendStatus.INVALID_PERSONAL_NAME, recipient.getSubject());
			}
			else {
				bccList.add(recipient.getSubject());
			}
		}

		Collection<String> bccEmails = new ArrayList<String>();
		for (Person person : bccList) {
			bccEmails.add(person.getEmail());
		}
		Collection<String> notSent = EmailSender.send(from.getPersonalName(), from.getAddress(), null, null, bccEmails, message.getSubject(), message.getText());
		for (Person person : bccList) {
			if (!notSent.contains(person.getEmail())) {
				report.add(SendStatus.SENT, person);
			}
			else {
				report.add(SendStatus.TRANSPORT_ERROR, person);
			}
		}

		return report;
	}

	public Collection<Recipient> getRecipients() {
		return recipients;
	}

}