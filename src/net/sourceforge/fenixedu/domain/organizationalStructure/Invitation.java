package net.sourceforge.fenixedu.domain.organizationalStructure;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Invitation extends Invitation_Base {

    public Invitation(Person person, Unit unit, Party responsible, YearMonthDay begin, YearMonthDay end) {
	super();
	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.INVITATION);
	
	setAccountabilityType(accountabilityType);
	setChildParty(person);
	setParentParty(unit);
	setResponsible(responsible);
	setBeginDate(begin);
	setEndDate(end);
	setTimeIntervalToLogin();
    }

    private void setTimeIntervalToLogin() {
	getInvitedPerson().getLoginIdentification().setBeginDateDateTime(getBeginDate().toDateTimeAtMidnight());
	getInvitedPerson().getLoginIdentification().setEndDateDateTime(getEndDate().toDateTimeAtMidnight());
    }

    @Override
    public void setResponsible(Party responsible) {
	if (responsible == null) {
	    throw new DomainException("error.invitation.empty.responsible");
	}
	super.setResponsible(responsible);
    }

    public Unit getHostUnit() {
	return (Unit) getParentParty();
    }

    public Person getInvitedPerson() {
	return (Person) getChildParty();
    }
}
