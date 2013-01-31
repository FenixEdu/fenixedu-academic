package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class ThesisServiceWithMailNotification extends FenixService {

	public void run(Thesis thesis) throws FenixServiceException {
		process(thesis);
		sendEmail(thesis);
	}

	abstract void process(Thesis thesis);

	private void sendEmail(Thesis thesis) {
		Sender sender = PersonSender.newInstance(AccessControl.getPerson());
		new Message(sender, null, getRecipients(thesis), getSubject(thesis), getMessage(thesis), "");
	}

	protected String getMessage(String key, Object... args) {
		return getMessage(key, new Locale("pt"), args);
	}

	protected String getMessage(String key, Locale locale, Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("resources.MessagingResources", locale);

		String message = bundle.getString(key);
		return MessageFormat.format(message, args);
	}

	private Set<Recipient> getRecipients(Thesis thesis) {
		Set<Recipient> recipients = new HashSet<Recipient>();
		for (Person person : getReceivers(thesis)) {
			recipients.add(Recipient.newInstance(new PersonGroup(person)));
		}
		return recipients;
	}

	protected abstract String getSubject(Thesis thesis);

	protected abstract String getMessage(Thesis thesis);

	protected abstract Collection<Person> getReceivers(Thesis thesis);

	//
	// Utility methods
	//

	protected static Set<Person> personSet(Person... persons) {
		Set<Person> result = new HashSet<Person>();

		for (Person person : persons) {
			if (person != null) {
				result.add(person);
			}
		}

		return result;
	}

	protected static Person getPerson(ThesisEvaluationParticipant participant) {
		if (participant == null) {
			return null;
		} else {
			return participant.getPerson();
		}
	}

}
