package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class AnnouncementBoardApproversBean implements Serializable {

    private Person person;
    private boolean approver;

    public AnnouncementBoardApproversBean(final Person person, final boolean approver) {
        setPerson(person);
        setApprover(approver);
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isApprover() {
        return approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

}
