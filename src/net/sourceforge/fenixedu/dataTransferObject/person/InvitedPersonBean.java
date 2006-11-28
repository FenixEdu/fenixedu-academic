package net.sourceforge.fenixedu.dataTransferObject.person;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class InvitedPersonBean extends ExternalPersonBean {
   
    private DomainReference<Person> invitedPersonReference;
    
    private DomainReference<Party> responsibleReference;
    
    private DomainReference<Person> responsiblePersonReference;

    private YearMonthDay begin;
    
    private YearMonthDay end;
      
    public InvitedPersonBean() {
	super();
    }
    
    public Person getInvitedPerson() {
	return invitedPersonReference == null ? null : invitedPersonReference.getObject();
    }

    public void setInvitedPerson(Person person) {
	this.invitedPersonReference = person == null ? null : new DomainReference<Person>(person);
    }
    
    public Party getResponsible() {
	return responsibleReference == null ? null : responsibleReference.getObject();
    }

    public void setResponsible(Party party) {
	this.responsibleReference = party == null ? null : new DomainReference<Party>(party);
    }
    
    public Person getResponsiblePerson() {
	return responsiblePersonReference == null ? null : responsiblePersonReference.getObject();
    }

    public void setResponsiblePerson(Person person) {	
	this.responsiblePersonReference = person == null ? null : new DomainReference<Person>(person);
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
