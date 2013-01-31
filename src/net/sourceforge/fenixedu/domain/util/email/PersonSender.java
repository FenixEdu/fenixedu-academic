package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import pt.ist.fenixWebFramework.services.Service;

public class PersonSender extends PersonSender_Base {

	public PersonSender() {
		super();
		setFromAddress(Sender.getNoreplyMail());
		addReplyTos(new CurrentUserReplyTo());
	}

	public PersonSender(final Person person) {
		this();
		setPerson(person);
		setFromName(person.getName());
		setMembers(new PersonGroup(person));
	}

	@Service
	public static PersonSender newInstance(final Person person) {
		return person.hasSender() ? person.getSender() : new PersonSender(person);
	}
}
