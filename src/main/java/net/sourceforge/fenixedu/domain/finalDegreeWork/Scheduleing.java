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
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class Scheduleing extends Scheduleing_Base {

    public Scheduleing() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setAllowSimultaneousCoorientationAndCompanion(false);
        setAttributionByTeachers(false);
        setAllowCandaciesOnlyForStudentsWithADissertationEnrolment(false);
        setCurrentProposalNumber(1);
        setMinimumNumberOfStudents(Integer.valueOf(1));
        setMaximumNumberOfStudents(Integer.valueOf(1));
    }

    public Date getEndOfProposalPeriod() {
        if (this.getEndOfProposalPeriodDate() != null && this.getEndOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
        this.setEndOfProposalPeriodDate(endOfProposalPeriod);
        this.setEndOfProposalPeriodTime(endOfProposalPeriod);
    }

    public Date getStartOfProposalPeriod() {
        if (this.getStartOfProposalPeriodDate() != null && this.getStartOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
        this.setStartOfProposalPeriodDate(startOfProposalPeriod);
        this.setStartOfProposalPeriodTime(startOfProposalPeriod);
    }

    public Date getStartOfCandidacyPeriod() {
        if (this.getStartOfCandidacyPeriodDate() != null && this.getStartOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod) {
        this.setStartOfCandidacyPeriodDate(startOfCandidacyPeriod);
        this.setStartOfCandidacyPeriodTime(startOfCandidacyPeriod);
    }

    public Date getEndOfCandidacyPeriod() {
        if (this.getEndOfCandidacyPeriodDate() != null && this.getEndOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod) {
        this.setEndOfCandidacyPeriodDate(endOfCandidacyPeriod);
        this.setEndOfCandidacyPeriodTime(endOfCandidacyPeriod);
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(getExecutionDegrees());
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        return executionDegrees;
    }

    public Set<Proposal> findProposalsByStatus(final FinalDegreeWorkProposalStatus finalDegreeWorkProposalStatus) {
        final Set<Proposal> proposals = new HashSet<Proposal>();
        for (final Proposal proposal : getProposalsSet()) {
            if (finalDegreeWorkProposalStatus.equals(proposal.getStatus())) {
                proposals.add(proposal);
            }
        }
        return proposals;
    }

    public Set<Proposal> findPublishedProposals() {
        return findProposalsByStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
    }

    public Set<Proposal> findApprovedProposals() {
        return findProposalsByStatus(FinalDegreeWorkProposalStatus.APPROVED_STATUS);
    }

    public SortedSet<FinalDegreeWorkGroup> getGroupsSortedByStudentNumbers() {
        final SortedSet<FinalDegreeWorkGroup> groups =
                new TreeSet<FinalDegreeWorkGroup>(FinalDegreeWorkGroup.COMPARATOR_BY_STUDENT_NUMBERS);
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            groups.addAll(executionDegree.getAssociatedFinalDegreeWorkGroupsSet());
        }
        return groups;
    }

    public SortedSet<FinalDegreeWorkGroup> getGroupsWithProposalsSortedByStudentNumbers() {
        final SortedSet<FinalDegreeWorkGroup> groups =
                new TreeSet<FinalDegreeWorkGroup>(FinalDegreeWorkGroup.COMPARATOR_BY_STUDENT_NUMBERS);
        for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
            for (final FinalDegreeWorkGroup group : executionDegree.getAssociatedFinalDegreeWorkGroupsSet()) {
                if (!group.getGroupProposalsSet().isEmpty()) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    public boolean isInsideProposalSubmissionPeriod() {
        if (getStartOfProposalPeriod() == null && getEndOfProposalPeriod() == null) {
            return false;
        }

        final DateTime start = new DateTime(getStartOfProposalPeriod());
        final DateTime end = new DateTime(getEndOfProposalPeriod());
        return !start.isAfterNow() && !end.isBeforeNow();
    }

    public void delete() {
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public Interval getProposalInterval() {
        final Date startDate = getStartOfProposalPeriodDate();
        final Date startTime = getStartOfProposalPeriodTime();
        final Date endDate = getEndOfProposalPeriodDate();
        final Date endTime = getEndOfProposalPeriodTime();

        return getInterval(startDate, startTime, endDate, endTime);
    }

    public Interval getCandidacyInterval() {
        final Date startDate = getStartOfCandidacyPeriodDate();
        final Date startTime = getStartOfCandidacyPeriodTime();
        final Date endDate = getEndOfCandidacyPeriodDate();
        final Date endTime = getEndOfCandidacyPeriodTime();

        return getInterval(startDate, startTime, endDate, endTime);
    }

    private Interval getInterval(final Date startDate, final Date startTime, final Date endDate, final Date endTime) {
        if (startDate != null && startTime != null && endDate != null && endTime != null) {
            final DateTime start = newDateTime(startDate, startTime);
            final DateTime end = newDateTime(endDate, endTime);
            if (start != null && end != null) {
                return new Interval(start, end);
            }
        }
        return null;
    }

    private DateTime newDateTime(final Date date, final Date time) {
        if (date != null && time != null) {
            return new DateTime(date.getTime()).withHourOfDay(time.getHours()).withMinuteOfHour(time.getMinutes())
                    .withSecondOfMinute(0).withMillisOfSecond(0);
        }
        return null;
    }

    public boolean getAreCandidacyConditionsDefined() {
        return getMinimumCompletedCreditsFirstCycle() != null && getMinimumCompletedCreditsSecondCycle() != null
                && getMaximumNumberOfProposalCandidaciesPerGroup() != null;
    }

    public ExecutionYear getExecutionYearOfOneExecutionDegree() {
        return getExecutionDegrees().iterator().next().getExecutionYear();
    }

    public Set<FinalDegreeWorkGroup> getAssociatedFinalDegreeWorkGroups() {
        final Set<FinalDegreeWorkGroup> groups = new HashSet<FinalDegreeWorkGroup>();
        for (ExecutionDegree executionDegree : getExecutionDegrees()) {
            groups.addAll(executionDegree.getAssociatedFinalDegreeWorkGroups());
        }
        return groups;
    }

    @Atomic
    public static Scheduleing newInstance(ExecutionDegree executionDegree) {
        if (!executionDegree.hasScheduling()) {
            final Scheduleing scheduling = new Scheduleing();
            executionDegree.setScheduling(scheduling);
            return scheduling;
        }
        return null;
    }

    public DateTime getStartOfProposalPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getStartOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getStartOfProposalPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }

    public void setStartOfProposalPeriodDateTime(final DateTime dateTime) {
        setStartOfProposalPeriodDateYearMonthDay(dateTime.toYearMonthDay());
        setStartOfProposalPeriodTimeHourMinuteSecond(new HourMinuteSecond(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute()));
    }

    public DateTime getEndOfProposalPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getEndOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getEndOfProposalPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }

    public void setEndOfProposalPeriodDateTime(final DateTime dateTime) {
        setEndOfProposalPeriodDateYearMonthDay(dateTime.toYearMonthDay());
        setEndOfProposalPeriodTimeHourMinuteSecond(new HourMinuteSecond(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute()));
    }

    public Interval getProposalPeriodInterval() {
        final YearMonthDay startYearMonthDay = getStartOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getStartOfProposalPeriodTimeHourMinuteSecond();
        final DateTime start = toDateTime(startYearMonthDay, startHourMinuteSecond);

        final YearMonthDay endYearMonthDay = getEndOfProposalPeriodDateYearMonthDay();
        final HourMinuteSecond endMinuteSecond = getEndOfProposalPeriodTimeHourMinuteSecond();
        final DateTime end = toDateTime(endYearMonthDay, endMinuteSecond);

        return toInterval(start, end);
    }

    public DateTime getStartOfCandidacyPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getStartOfCandidacyPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getStartOfCandidacyPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }

    public void setStartOfCandidacyPeriodDateTime(final DateTime dateTime) {
        setStartOfCandidacyPeriodDateYearMonthDay(dateTime.toYearMonthDay());
        setStartOfCandidacyPeriodTimeHourMinuteSecond(new HourMinuteSecond(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute()));
    }

    public DateTime getEndOfCandidacyPeriodDateTime() {
        final YearMonthDay startYearMonthDay = getEndOfCandidacyPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getEndOfCandidacyPeriodTimeHourMinuteSecond();
        return toDateTime(startYearMonthDay, startHourMinuteSecond);
    }

    public void setEndOfCandidacyPeriodDateTime(final DateTime dateTime) {
        setEndOfCandidacyPeriodDateYearMonthDay(dateTime.toYearMonthDay());
        setEndOfCandidacyPeriodTimeHourMinuteSecond(new HourMinuteSecond(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute()));
    }

    public Interval getCandidacyPeriodInterval() {
        final YearMonthDay startYearMonthDay = getStartOfCandidacyPeriodDateYearMonthDay();
        final HourMinuteSecond startHourMinuteSecond = getStartOfCandidacyPeriodTimeHourMinuteSecond();
        final DateTime start = toDateTime(startYearMonthDay, startHourMinuteSecond);

        final YearMonthDay endYearMonthDay = getEndOfCandidacyPeriodDateYearMonthDay();
        final HourMinuteSecond endMinuteSecond = getEndOfCandidacyPeriodTimeHourMinuteSecond();
        final DateTime end = toDateTime(endYearMonthDay, endMinuteSecond);

        return toInterval(start, end);
    }

    private Interval toInterval(final DateTime start, final DateTime end) {
        return start == null || end == null || !end.isAfter(start) ? null : new Interval(start, end);
    }

    private DateTime toDateTime(final YearMonthDay yearMonthDay, final HourMinuteSecond hourMinuteSecond) {
        return yearMonthDay == null || hourMinuteSecond == null ? null : new DateTime(yearMonthDay.getYear(),
                yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), hourMinuteSecond.getHour(),
                hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }

    @Deprecated
    public java.util.Date getEndOfCandidacyPeriodDate() {
        org.joda.time.YearMonthDay ymd = getEndOfCandidacyPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndOfCandidacyPeriodDate(java.util.Date date) {
        if (date == null) {
            setEndOfCandidacyPeriodDateYearMonthDay(null);
        } else {
            setEndOfCandidacyPeriodDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndOfCandidacyPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndOfCandidacyPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndOfCandidacyPeriodTime(java.util.Date date) {
        if (date == null) {
            setEndOfCandidacyPeriodTimeHourMinuteSecond(null);
        } else {
            setEndOfCandidacyPeriodTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndOfProposalPeriodDate() {
        org.joda.time.YearMonthDay ymd = getEndOfProposalPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndOfProposalPeriodDate(java.util.Date date) {
        if (date == null) {
            setEndOfProposalPeriodDateYearMonthDay(null);
        } else {
            setEndOfProposalPeriodDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndOfProposalPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndOfProposalPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndOfProposalPeriodTime(java.util.Date date) {
        if (date == null) {
            setEndOfProposalPeriodTimeHourMinuteSecond(null);
        } else {
            setEndOfProposalPeriodTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartOfCandidacyPeriodDate() {
        org.joda.time.YearMonthDay ymd = getStartOfCandidacyPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartOfCandidacyPeriodDate(java.util.Date date) {
        if (date == null) {
            setStartOfCandidacyPeriodDateYearMonthDay(null);
        } else {
            setStartOfCandidacyPeriodDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartOfCandidacyPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getStartOfCandidacyPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartOfCandidacyPeriodTime(java.util.Date date) {
        if (date == null) {
            setStartOfCandidacyPeriodTimeHourMinuteSecond(null);
        } else {
            setStartOfCandidacyPeriodTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartOfProposalPeriodDate() {
        org.joda.time.YearMonthDay ymd = getStartOfProposalPeriodDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartOfProposalPeriodDate(java.util.Date date) {
        if (date == null) {
            setStartOfProposalPeriodDateYearMonthDay(null);
        } else {
            setStartOfProposalPeriodDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartOfProposalPeriodTime() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getStartOfProposalPeriodTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartOfProposalPeriodTime(java.util.Date date) {
        if (date == null) {
            setStartOfProposalPeriodTimeHourMinuteSecond(null);
        } else {
            setStartOfProposalPeriodTimeHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal> getProposals() {
        return getProposalsSet();
    }

    @Deprecated
    public boolean hasAnyProposals() {
        return !getProposalsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionDegree> getExecutionDegrees() {
        return getExecutionDegreesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegrees() {
        return !getExecutionDegreesSet().isEmpty();
    }

    @Deprecated
    public boolean hasMinimumCompletedCurricularYear() {
        return getMinimumCompletedCurricularYear() != null;
    }

    @Deprecated
    public boolean hasAttributionByTeachers() {
        return getAttributionByTeachers() != null;
    }

    @Deprecated
    public boolean hasMinimumCompletedCreditsSecondCycle() {
        return getMinimumCompletedCreditsSecondCycle() != null;
    }

    @Deprecated
    public boolean hasCurrentProposalNumber() {
        return getCurrentProposalNumber() != null;
    }

    @Deprecated
    public boolean hasMaximumNumberOfProposalCandidaciesPerGroup() {
        return getMaximumNumberOfProposalCandidaciesPerGroup() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMinimumCompletedCreditsFirstCycle() {
        return getMinimumCompletedCreditsFirstCycle() != null;
    }

    @Deprecated
    public boolean hasMaximumCurricularYearToCountCompletedCourses() {
        return getMaximumCurricularYearToCountCompletedCourses() != null;
    }

    @Deprecated
    public boolean hasStartOfCandidacyPeriodDateYearMonthDay() {
        return getStartOfCandidacyPeriodDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasEndOfCandidacyPeriodTimeHourMinuteSecond() {
        return getEndOfCandidacyPeriodTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasMinimumNumberOfCompletedCourses() {
        return getMinimumNumberOfCompletedCourses() != null;
    }

    @Deprecated
    public boolean hasEndOfCandidacyPeriodDateYearMonthDay() {
        return getEndOfCandidacyPeriodDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasMinimumNumberOfStudents() {
        return getMinimumNumberOfStudents() != null;
    }

    @Deprecated
    public boolean hasStartOfCandidacyPeriodTimeHourMinuteSecond() {
        return getStartOfCandidacyPeriodTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasMaximumNumberOfProposalsPerPerson() {
        return getMaximumNumberOfProposalsPerPerson() != null;
    }

    @Deprecated
    public boolean hasEndOfProposalPeriodTimeHourMinuteSecond() {
        return getEndOfProposalPeriodTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasMaximumNumberOfStudents() {
        return getMaximumNumberOfStudents() != null;
    }

    @Deprecated
    public boolean hasStartOfProposalPeriodTimeHourMinuteSecond() {
        return getStartOfProposalPeriodTimeHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasAllowCandaciesOnlyForStudentsWithADissertationEnrolment() {
        return getAllowCandaciesOnlyForStudentsWithADissertationEnrolment() != null;
    }

    @Deprecated
    public boolean hasStartOfProposalPeriodDateYearMonthDay() {
        return getStartOfProposalPeriodDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasAllowSimultaneousCoorientationAndCompanion() {
        return getAllowSimultaneousCoorientationAndCompanion() != null;
    }

    @Deprecated
    public boolean hasEndOfProposalPeriodDateYearMonthDay() {
        return getEndOfProposalPeriodDateYearMonthDay() != null;
    }

}
