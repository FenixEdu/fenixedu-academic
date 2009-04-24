package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import pt.ist.fenixWebFramework.services.Service;

public class PersonSender extends PersonSender_Base {

    public PersonSender() {
	super();
    }

    public PersonSender(Person person) {
	super();
	setPerson(person);
	setFromName(person.getName());
	setFromAddress("noreply@ist.utl.pt");
	addReplyTos(new CurrentUserReplyTo());
	setMembers(new PersonGroup(person));
    }

    @Service
    public static PersonSender newInstance(Person person) {
	if (person.hasSender()) {
	    return person.getSender();
	}
	return new PersonSender(person);
    }
}
