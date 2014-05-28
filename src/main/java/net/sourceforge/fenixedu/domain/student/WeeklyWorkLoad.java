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
package net.sourceforge.fenixedu.domain.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;

public class WeeklyWorkLoad extends WeeklyWorkLoad_Base implements Comparable<WeeklyWorkLoad> {

    public WeeklyWorkLoad(final Attends attends, final Integer weekOffset, final Integer contact, final Integer autonomousStudy,
            final Integer other) {
        super();
        setRootDomainObject(Bennu.getInstance());

        if (attends == null || weekOffset == null) {
            throw new NullPointerException();
        }

        setAttends(attends);
        setContact(contact);
        setAutonomousStudy(autonomousStudy);
        setOther(other);
        setWeekOffset(weekOffset);
    }

    public int getTotal() {
        final int contact = getContact() != null ? getContact() : 0;
        final int autonomousStudy = getAutonomousStudy() != null ? getAutonomousStudy() : 0;
        final int other = getOther() != null ? getOther() : 0;
        return contact + autonomousStudy + other;
    }

    @Override
    public int compareTo(final WeeklyWorkLoad weeklyWorkLoad) {
        if (weeklyWorkLoad == null) {
            throw new NullPointerException("Cannot compare weekly work load with null");
        }
        if (getAttends() != weeklyWorkLoad.getAttends()) {
            throw new IllegalArgumentException("Cannot compare weekly work loads of different attends.");
        }

        return getWeekOffset().compareTo(weeklyWorkLoad.getWeekOffset());
    }

    public Interval getInterval() {
        final DateTime beginningOfSemester = new DateTime(getAttends().getBegginingOfLessonPeriod());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime start = firstMonday.withFieldAdded(DurationFieldType.weeks(), getWeekOffset().intValue());
        final DateTime end = start.plusWeeks(1);
        return new Interval(start, end);
    }

    public void delete() {
        check(this, RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE);
        setAttends(null);
        setRootDomainObject(null);

        super.deleteDomainObject();

    }

    @Deprecated
    public boolean hasWeekOffset() {
        return getWeekOffset() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasContact() {
        return getContact() != null;
    }

    @Deprecated
    public boolean hasOther() {
        return getOther() != null;
    }

    @Deprecated
    public boolean hasAttends() {
        return getAttends() != null;
    }

    @Deprecated
    public boolean hasAutonomousStudy() {
        return getAutonomousStudy() != null;
    }

}
