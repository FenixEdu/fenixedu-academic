package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.UsernameCounter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Invitation extends Invitation_Base {

    private static final int MAX_USER_UID = 99999;
    
    public static final Comparator<Invitation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new ReverseComparator(new BeanComparator("beginDate")));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    public Invitation(Person person, Unit unit, Party responsible, YearMonthDay begin, YearMonthDay end) {
	
	super();
	
	AccountabilityType accountabilityType = getInvitationAccountabilityType();

	setAccountabilityType(accountabilityType);
	setChildParty(person);
	setParentParty(unit);
	setResponsible(responsible);
	setInvitationInterval(begin, end);	
	
	if(isActive(new YearMonthDay())) {
	    person.addPersonRoleByRoleType(RoleType.PERSON);
	}
    }

    public void setInvitationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkInvitationDatesIntersection(getInvitedPerson(), beginDate, endDate);
	if (getBeginDate() == null) {
	    new LoginPeriod(beginDate, endDate, getInvitedPerson().getLoginIdentification());
	} else {
	    editLoginPeriod(getBeginDate(), getEndDate(), beginDate, endDate);
	}
	super.setBeginDate(beginDate);
	super.setEndDate(endDate);
    }
        
    @Override
    public void setChildParty(Party childParty) {
        if(!childParty.isPerson()) {
            throw new DomainException("error.invalid.child.party");
        }
	super.setChildParty(childParty);
    }
    
    @Override
    public void setParentParty(Party parentParty) {
	if(!parentParty.isUnit()) {
            throw new DomainException("error.invalid.parent.party");
        }
	super.setParentParty(parentParty);
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
	throw new DomainException("error.Invitation.invalid.operation");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	throw new DomainException("error.Invitation.invalid.operation");
    }
    
    public Unit getHostUnit() {
	return (Unit) getParentParty();
    }

    public Person getInvitedPerson() {
	return (Person) getChildParty();
    }

    public Person getResponsiblePerson() {
	return (getResponsible().isPerson()) ? (Person) getResponsible() : null;
    }

    public void setResponsiblePerson(Person person) {
	setResponsible(person);
    }

    @Override
    public void delete() {
	LoginPeriod period = getInvitedPerson().getLoginIdentification().readLoginPeriodByTimeInterval(getBeginDate(), getEndDate());
	if (period != null) {
	    period.delete();
	}
	super.setResponsible(null);
	super.delete();
    }

    private void checkInvitationDatesIntersection(Person person, YearMonthDay begin, YearMonthDay end) {
	checkBeginDateAndEndDate(begin, end);
	for (Invitation invitation : (Collection<Invitation>) person.getParentAccountabilities(
		AccountabilityTypeEnum.INVITATION, Invitation.class)) {
	    if (!invitation.equals(this) && invitation.getHostUnit().equals(this.getHostUnit())
		    && invitation.getResponsible().equals(this.getResponsible())
		    && invitation.hasDatesIntersection(begin, end)) {
		throw new DomainException("error.invitation.dates.intersection");
	    }
	}
    }

    private boolean hasDatesIntersection(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || !getBeginDate().isAfter(end)) && (getEndDate() == null || !getEndDate().isBefore(begin)));
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

    private void editLoginPeriod(YearMonthDay oldBegin, YearMonthDay oldEnd, YearMonthDay newBeginDate,
	    YearMonthDay newEndDate) {

	Login login = getInvitedPerson().getLoginIdentification();
	LoginPeriod period = login.readLoginPeriodByTimeInterval(oldBegin, oldEnd);
	if (period != null) {
	    period.edit(newBeginDate, newEndDate);
	} else {
	    new LoginPeriod(newBeginDate, newEndDate, getInvitedPerson().getLoginIdentification());
	}
    }
    
    public static AccountabilityType getInvitationAccountabilityType() {
	return AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.INVITATION);
    }

    public static int nextUserIDForInvitedPerson() {
	final UsernameCounter usernameCounter = RootDomainObject.getInstance().getUsernameCounter();
	final int nextUserID = usernameCounter.getInvitationCounter().intValue();
	usernameCounter.setInvitationCounter(Integer.valueOf(nextUserID + 1));
	if (nextUserID > MAX_USER_UID) {
	    throw new DomainException("error.invitation.uid.pool.exhausted");
	}
	return nextUserID;
    }
}
