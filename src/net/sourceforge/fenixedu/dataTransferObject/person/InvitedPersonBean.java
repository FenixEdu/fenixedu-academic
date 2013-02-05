package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.YearMonthDay;

public class InvitedPersonBean extends ExternalPersonBean {

    private Person invitedPersonReference;

    private Party responsibleReference;

    private Person responsiblePersonReference;

    private YearMonthDay begin;

    private YearMonthDay end;

    public InvitedPersonBean() {
        super();
    }

    public Person getInvitedPerson() {
        return invitedPersonReference;
    }

    public void setInvitedPerson(Person person) {
        this.invitedPersonReference = person;
    }

    public Party getResponsible() {
        return responsibleReference;
    }

    public void setResponsible(Party party) {
        this.responsibleReference = party;
    }

    public Person getResponsiblePerson() {
        return responsiblePersonReference;
    }

    public void setResponsiblePerson(Person person) {
        this.responsiblePersonReference = person;
        setResponsible(person);
    }

    public YearMonthDay getBegin() {
        return begin;
    }

    public void setBegin(YearMonthDay begin) {
        this.begin = begin;
    }

    public YearMonthDay getEnd() {
        return end;
    }

    public void setEnd(YearMonthDay end) {
        this.end = end;
    }
}
