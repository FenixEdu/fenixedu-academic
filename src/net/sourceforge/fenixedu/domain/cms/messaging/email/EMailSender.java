/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.RecipientType;
import net.sourceforge.fenixedu.domain.cms.messaging.email.Recipient.SendStatus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
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
		if (persons != null) {
			for (Person person : persons) {
				PersonRecipient recipient = new PersonRecipient(person);
				recipient.setType(type);
				this.recipients.add(recipient);
			}
		}
	}

	public void addRecipient(RecipientType type, Collection<Recipient> recipients) {
		if (recipients != null) {
			this.recipients.addAll(recipients);
		}
	}

	public void addRecipient(RecipientType type, List<IGroup> list) {
		if (list != null) {
			IGroup[] groups = new IGroup[list.size()];
			for (int i = 0; i < groups.length; i++) {
				groups[i] = list.get(i);
			}
			this.addRecipient(type, groups);
		}
	}

	public void addRecipient(RecipientType type, IGroup... groups) {
		if (groups != null) {
			for (IGroup group : groups) {
				for (Person person : group.getElements()) {
					this.addRecipient(type, person);
				}
			}
		}
	}

	public void addRecipient(RecipientType type, EMailAddress... recipients) {
		if (recipients != null) {
			for (EMailAddress address : recipients) {
				AddressRecipient recipient = new AddressRecipient(address);
				recipient.setType(type);
				this.recipients.add(recipient);
			}
		}
	}

	public SendMailReport send(EMailAddress from, boolean copyToSender) throws SenderNotAllowed {
		if (!checkIfSenderIsAllowed()) {
			throw new SenderNotAllowed();
		}

		return this.sendToAll(message, from, copyToSender);

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

	private SendMailReport sendToAll(EMailMessage message, EMailAddress from, boolean copyToSender) {

		SendMailReport report = new SendMailReport();
		Collection<Person> bccListPerson = new ArrayList<Person>();
		Collection<EMailAddress> bccListAddress = new ArrayList<EMailAddress>();

		if (copyToSender) {
			bccListAddress.add(from);
		}
		
		for (Recipient recipient : this.recipients) {
			if (!recipient.getAddress().isValid()) {
				if (recipient instanceof PersonRecipient) {
					report.add(SendStatus.INVALID_ADDRESS, ((PersonRecipient) recipient).getSubject());
				}
				else if (recipient instanceof AddressRecipient) {
					report.add(SendStatus.INVALID_ADDRESS, recipient.getAddress());
				}
			}
			else if (recipient instanceof PersonRecipient
					&& !EMailAddress.isValid(((PersonRecipient) recipient).getSubject().getNome(), ((PersonRecipient) recipient).getSubject().getEmail())) {
				report.add(SendStatus.INVALID_PERSONAL_NAME, ((PersonRecipient) recipient).getSubject());
			}
			else {
				if (recipient instanceof PersonRecipient) {
					bccListPerson.add(((PersonRecipient) recipient).getSubject());
				}
				else if (recipient instanceof AddressRecipient) {
					bccListAddress.add(((AddressRecipient) recipient).getAddress());
				}

			}
		}

		Collection<String> bccEmails = new ArrayList<String>();
		for (Person person : bccListPerson) {
			bccEmails.add(person.getEmail());
		}
		for (EMailAddress address : bccListAddress) {
			bccEmails.add(address.getAddress());
		}

		Collection<String> notSent = EmailSender.send(from.getPersonalName(), from.getAddress(), null, null, bccEmails, message.getSubject(), message.getText());
		
		for (EMailAddress address : bccListAddress) {
			if (!notSent.contains(address.getAddress())) {
				report.add(SendStatus.SENT, address);
			}
			else {
				report.add(SendStatus.TRANSPORT_ERROR, address);
			}
		}
		
		for (Person person : bccListPerson) {
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