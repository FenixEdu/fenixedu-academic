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
package org.fenixedu.academic.domain.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class UnavailablePeriod extends UnavailablePeriod_Base {

    public UnavailablePeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public UnavailablePeriod(DateTime beginDate, DateTime endDate, String justification) {
        this();
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setJustification(justification);
    }

    public UnavailablePeriod(DateTime beginDate, DateTime endDate, String justification, Person person) {
        this();
        this.setPerson(person);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setJustification(justification);
    }

    @Override
    public void setBeginDate(DateTime begin) {
        DateTime currentTime = new DateTime();
        if ((isExamCoordinatorRequesting(this.getPerson()) || begin.isAfter(currentTime))
                && (this.getEndDate() == null || this.getEndDate().isAfter(begin))) {
            super.setBeginDate(begin);
        } else {
            throw new DomainException("label.vigilancy.error.invalidBeginDate");
        }
    }

    @Override
    public void setEndDate(DateTime end) {
        if (this.getBeginDate() == null || this.getBeginDate().isBefore(end)) {
            super.setEndDate(end);
        } else {
            throw new DomainException("label.vigilancy.error.invalidEndDate");
        }
    }

    public Boolean containsDate(DateTime date) {
        Interval interval = new Interval(this.getBeginDate(), this.getEndDate());
        return interval.contains(date);
    }

    public Boolean containsInterval(DateTime begin, DateTime end) {
        Interval interval = new Interval(this.getBeginDate(), this.getEndDate());
        return (interval.contains(begin) || interval.contains(end));
    }

    private void doActualDelete() {
        setRootDomainObject(null);
        this.getPerson().removeUnavailablePeriods(this);
        setPerson(null);
        super.deleteDomainObject();
    }

    public void delete() {
        if (isExamCoordinatorRequesting(this.getPerson())) {
            this.doActualDelete();
        } else {
            if (this.canChangeUnavailablePeriodFor(this.getPerson())) {
                this.doActualDelete();
            }
        }
    }

    private void doActualEdit(DateTime begin, DateTime end, String justification) {
        this.setBeginDate(begin);
        this.setEndDate(end);
        this.setJustification(justification);
    }

    public void edit(DateTime begin, DateTime end, String justification) {

        if (isExamCoordinatorRequesting(this.getPerson())) {
            this.doActualEdit(begin, end, justification);
        } else {
            if (this.canChangeUnavailablePeriodFor(this.getPerson())) {
                this.doActualEdit(begin, end, justification);
            }
        }
    }

    private boolean canChangeUnavailablePeriodFor(Person person) {
        if (person != null) {
            if (!isAllowedToSpecifyUnavailablePeriod(person)) {
                throw new DomainException("vigilancy.error.outOutPeriodToSpecifyUnavailablePeriods");
            }
            if (this.getEndDate() != null && this.getEndDate().isBeforeNow()) {
                throw new DomainException("vigilancy.error.cannotEditClosedUnavailablePeriod");
            }
            List<Vigilancy> convokes = Vigilancy.getVigilanciesForYear(person, ExecutionYear.readCurrentExecutionYear());
            for (Vigilancy convoke : convokes) {
                WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
                DateTime begin = writtenEvaluation.getBeginningDateTime();
                DateTime end = writtenEvaluation.getEndDateTime();
                if (this.containsInterval(begin, end)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isExamCoordinatorRequesting(Person person) {

        User userview = Authenticate.getUser();

        if (userview != null) {
            Person loggedPerson = userview.getPerson();
            ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
            List<VigilantGroup> vigilantGroups = VigilantGroup.getVigilantGroupsForExecutionYear(person, executionYear);

            if (loggedPerson != null) {
                ExamCoordinator coordinator =
                        ExamCoordinator.getExamCoordinatorForGivenExecutionYear(loggedPerson, executionYear);
                if (coordinator == null) {
                    return false;
                } else {
                    for (VigilantGroup group : vigilantGroups) {
                        if (coordinator.managesGivenVigilantGroup(group)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getUnavailableAsString() {
        DateTime begin = this.getBeginDate();
        DateTime end = this.getEndDate();

        return String.format("%02d/%02d/%d (%02d:%02d) - %02d/%02d/%d (%02d:%02d): %s", begin.getDayOfMonth(),
                begin.getMonthOfYear(), begin.getYear(), begin.getHourOfDay(), begin.getMinuteOfHour(), end.getDayOfMonth(),
                end.getMonthOfYear(), end.getYear(), end.getHourOfDay(), end.getMinuteOfHour(), this.getJustification());
    }

    private static boolean isAllowedToSpecifyUnavailablePeriod(Person person) {
        final DateTime currentDate = new DateTime();
        final List<VigilantGroup> groupsForYear =
                VigilantGroup.getVigilantGroupsForExecutionYear(person, ExecutionYear.readCurrentExecutionYear());
        for (final VigilantGroup group : groupsForYear) {
            if (group.canSpecifyUnavailablePeriodIn(currentDate)) {
                return true;
            }
        }
        return false;
    }

    public static List<UnavailablePeriod> getUnavailablePeriodsForGivenYear(Person person, ExecutionYear executionYear) {
        final Collection<UnavailablePeriod> unavailablePeriods = person.getUnavailablePeriodsSet();
        final List<UnavailablePeriod> unavailablePeriodsForGivenYear = new ArrayList<UnavailablePeriod>();
        for (final UnavailablePeriod unavailablePeriod : unavailablePeriods) {
            if (unavailablePeriod.getBeginDate().getYear() == executionYear.getBeginCivilYear()
                    || unavailablePeriod.getBeginDate().getYear() == executionYear.getEndCivilYear()) {
                unavailablePeriodsForGivenYear.add(unavailablePeriod);
            }
        }
        return unavailablePeriodsForGivenYear;
    }

}
