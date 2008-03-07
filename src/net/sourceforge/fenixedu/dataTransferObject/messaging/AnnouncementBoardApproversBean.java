package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

public class AnnouncementBoardApproversBean implements Serializable {

    private DomainReference<Person> person;
    private boolean approver;

    public AnnouncementBoardApproversBean(final Person person, final boolean approver) {
	setPerson(person);
	setApprover(approver);
    }

    public Person getPerson() {
	return (this.person != null) ? this.person.getObject() : null;
    }

    public void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public boolean isApprover() {
	return approver;
    }

    public void setApprover(boolean approver) {
	this.approver = approver;
    }

}
