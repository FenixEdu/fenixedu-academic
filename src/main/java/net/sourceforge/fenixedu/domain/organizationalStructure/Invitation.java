package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.YearMonthDay;

public class Invitation extends Invitation_Base {

    public Invitation(Person person, Unit unit, Party responsible, YearMonthDay begin, YearMonthDay end) {

        super();

        AccountabilityType accountabilityType = getInvitationAccountabilityType();

        setAccountabilityType(accountabilityType);
        setChildParty(person);
        setParentParty(unit);
        setResponsible(responsible);
        person.addPersonRoleByRoleType(RoleType.PERSON);
        setInvitationInterval(begin, end);
    }

    public void setInvitationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
        checkInvitationDatesIntersection(getInvitedPerson(), beginDate, endDate);
        super.setBeginDate(beginDate);
        super.setEndDate(endDate);
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
        return getUnit();
    }

    public Person getInvitedPerson() {
        return getPerson();
    }

    public Person getResponsiblePerson() {
        return (getResponsible().isPerson()) ? (Person) getResponsible() : null;
    }

    public void setResponsiblePerson(Person person) {
        setResponsible(person);
    }

    @Override
    public void delete() {
        super.setResponsible(null);
        super.delete();
    }

    private void checkInvitationDatesIntersection(Person person, YearMonthDay begin, YearMonthDay end) {
        checkBeginDateAndEndDate(begin, end);
        for (Invitation invitation : (Collection<Invitation>) person.getParentAccountabilities(AccountabilityTypeEnum.INVITATION,
                Invitation.class)) {
            if (!invitation.equals(this) && invitation.getHostUnit().equals(this.getHostUnit())
                    && invitation.getResponsible().equals(this.getResponsible()) && invitation.hasDatesIntersection(begin, end)) {
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

    public static AccountabilityType getInvitationAccountabilityType() {
        return AccountabilityType.readByType(AccountabilityTypeEnum.INVITATION);
    }

    @Deprecated
    public boolean hasResponsible() {
        return getResponsible() != null;
    }

}
