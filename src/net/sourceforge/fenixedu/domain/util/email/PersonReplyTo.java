package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;

public class PersonReplyTo extends PersonReplyTo_Base {

    public PersonReplyTo(Person person) {
	super();
	setPerson(person);
	// System.out.println("Person has new ReplyTo : " + person.getName());
    }

    @Override
    public String getReplyToAddress() {
	return getPerson().getDefaultEmailAddress().getValue();
    }

}
