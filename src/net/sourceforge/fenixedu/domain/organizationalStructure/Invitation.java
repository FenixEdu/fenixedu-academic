package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Invitation extends Invitation_Base {

    public static final Comparator<Invitation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new ReverseComparator(new BeanComparator("beginDate")));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }

    public Invitation(Person person, Unit unit, Party responsible, YearMonthDay begin, YearMonthDay end) {
	super();
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.INVITATION);

	setAccountabilityType(accountabilityType);
	setChildParty(person);
	setParentParty(unit);
	setResponsible(responsible);
	setInvitationInterval(begin, end);
    }
   
    @Override
    public void setResponsible(Party responsible) {
	if (responsible == null) {
	    throw new DomainException("error.invitation.empty.responsible");
	}
	super.setResponsible(responsible);
    }    

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	checkInvitationDatesIntersection(getInvitedPerson(), beginDate, getEndDate());
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	checkInvitationDatesIntersection(getInvitedPerson(), getBeginDate(), endDate);
	super.setEndDate(endDate);
    }

    public void setInvitationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkInvitationDatesIntersection(getInvitedPerson(), beginDate, endDate);
	super.setBeginDate(beginDate);
	super.setEndDate(endDate);
    }

    public Unit getHostUnit() {
	return (Unit) getParentParty();
    }

    public Person getInvitedPerson() {
	return (Person) getChildParty();
    }
    
    public Person getResponsiblePerson() {
	return (getResponsible().isPerson()) ? (Person)getResponsible() : null;
    }
    
    public void setResponsiblePerson(Person person) {
	setResponsible(person);
    }
    
    private void checkInvitationDatesIntersection(Person person, YearMonthDay begin, YearMonthDay end) {
	checkBeginDateAndEndDate(begin, end);
	for (Invitation invitation : (Collection<Invitation>) person.getParentAccountabilities(
		AccountabilityTypeEnum.INVITATION, Invitation.class)) {
	    if (!invitation.equals(this) && invitation.checkDatesIntersections(begin, end)) {
		throw new DomainException("error.invitation.dates.intersection");
	    }
	}
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !this.getBeginDate().isAfter(end)) && (this.getEndDate() == null || !this
		.getEndDate().isBefore(begin)));
    }

    private void checkBeginDateAndEndDate(YearMonthDay begin, YearMonthDay end) {
	if (begin == null) {
	    throw new DomainException("error.invitation.no.beginDate");
	}
	if (end == null) {
	    throw new DomainException("error.invitation.no.endDate");
	}
	if (end != null && !end.isAfter(begin)) {
	    throw new DomainException("error.invitation.endDateBeforeBeginDate");
	}
    }
}
