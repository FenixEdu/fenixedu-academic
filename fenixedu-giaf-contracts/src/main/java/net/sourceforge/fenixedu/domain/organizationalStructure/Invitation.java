/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.UserLoginPeriod;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class Invitation extends Invitation_Base {

    public Invitation(Person person, Unit unit, Party responsible, YearMonthDay begin, YearMonthDay end) {

        super();

        AccountabilityType accountabilityType = getInvitationAccountabilityType();

        setAccountabilityType(accountabilityType);
        setChildParty(person);
        setParentParty(unit);
        setResponsible(responsible);
        RoleType.grant(RoleType.PERSON, person.getUser());
        setInvitationInterval(begin, end);
    }

    public void setInvitationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
        checkInvitationDatesIntersection(getInvitedPerson(), beginDate, endDate);
        if (getBeginDate() == null) {
            // When editing from the constructor
            new UserLoginPeriod(getInvitedPerson().getUser(), beginDate.toLocalDate(), endDate.toLocalDate());
        } else {
            // When editing from the functionality
            editLoginPeriod(beginDate.toLocalDate(), endDate.toLocalDate());
        }
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
        UserLoginPeriod period = readExactPeriod();
        if (period != null) {
            period.delete();
        }
        super.setResponsible(null);
        super.delete();
    }

    private UserLoginPeriod readExactPeriod() {
        for (UserLoginPeriod period : getInvitedPerson().getUser().getLoginValiditySet()) {
            if (period.matches(getBeginDate().toLocalDate(), getEndDate().toLocalDate())) {
                return period;
            }
        }
        return null;
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

    private void editLoginPeriod(LocalDate beginDate, LocalDate endDate) {
        UserLoginPeriod period = readExactPeriod();
        if (period != null) {
            period.edit(beginDate, endDate);
        } else {
            new UserLoginPeriod(getInvitedPerson().getUser(), beginDate, endDate);
        }
    }

    public static AccountabilityType getInvitationAccountabilityType() {
        return AccountabilityType.readByType(AccountabilityTypeEnum.INVITATION);
    }

    public static Collection<Invitation> getInvitationsOrderByDate(Person person) {
        final Set<Invitation> invitations = new TreeSet<Invitation>(Invitation.CONTRACT_COMPARATOR_BY_BEGIN_DATE);
        invitations.addAll((Collection<Invitation>) person.getParentAccountabilities(AccountabilityTypeEnum.INVITATION,
                Invitation.class));
        return invitations;
    }

    public static List<Invitation> getActiveInvitations(Person person) {
        final YearMonthDay today = new YearMonthDay();
        final List<Invitation> invitations = new ArrayList<Invitation>();
        for (final Accountability accoutAccountability : person.getParentAccountabilities(AccountabilityTypeEnum.INVITATION,
                Invitation.class)) {
            if (((Invitation) accoutAccountability).isActive(today)) {
                invitations.add((Invitation) accoutAccountability);
            }
        }
        return invitations;
    }
}
