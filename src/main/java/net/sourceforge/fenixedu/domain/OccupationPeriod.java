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
/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.date.IntervalTools;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author Ana e Ricardo
 * 
 */
@SuppressWarnings("deprecation")
public class OccupationPeriod extends OccupationPeriod_Base {

    private OccupationPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public OccupationPeriod(Interval interval) {
        this();
        if (interval == null) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setPeriodInterval(interval);
    }

    public OccupationPeriod(LocalDate startDate, LocalDate endDate) {
        this();
        if (startDate == null || endDate == null) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setPeriodInterval(IntervalTools.getInterval(startDate, endDate));
    }

    public OccupationPeriod(DateTime startDate, DateTime endDate) {
        this();
        if (startDate == null || endDate == null) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setPeriodInterval(IntervalTools.getInterval(startDate, endDate));
    }

    public OccupationPeriod(Date startDate, Date endDate) {
        this();
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setPeriodInterval(IntervalTools.getInterval(startDate, endDate));
    }

    /**
     * Constructor that creates and links together several instances, allowing
     * for the definition of all the intervals
     * 
     * @param intervals
     */
    public OccupationPeriod(Iterator<Interval> intervals) {
        this();
        if (intervals == null || !intervals.hasNext()) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }

        Interval interval = intervals.next();

        this.setPeriodInterval(interval);

        if (intervals.hasNext()) {
            this.setNextPeriod(new OccupationPeriod(intervals));
        }
    }

    /*
     * Deprecated Constructors
     */

    @Deprecated
    public OccupationPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        this();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setPeriodInterval(IntervalTools.getInterval(startDate, endDate));
    }

    @Deprecated
    public OccupationPeriod(final YearMonthDay... yearMonthDays) {
        this(null, yearMonthDays);
    }

    @Deprecated
    protected OccupationPeriod(final OccupationPeriod previous, final YearMonthDay... yearMonthDays) {
        this();
        final int l = yearMonthDays.length;
        if (yearMonthDays == null || l < 2) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }

        final YearMonthDay start = yearMonthDays[0];
        final YearMonthDay end = yearMonthDays[1];
        this.setPeriodInterval(IntervalTools.getInterval(start, end));

        if (l > 2) {
            final YearMonthDay[] nextYearMonthDays = new YearMonthDay[l - 2];
            System.arraycopy(yearMonthDays, 2, nextYearMonthDays, 0, l - 2);
            new OccupationPeriod(this, nextYearMonthDays);
        }

        setPreviousPeriod(previous);
    }

    public void setNextPeriodWithoutChecks(OccupationPeriod nextPeriod) {
        if (nextPeriod != null && !nextPeriod.getPeriodInterval().isAfter(getPeriodInterval())) {
            throw new DomainException("error.occupationPeriod.invalid.nextPeriod");
        }
        super.setNextPeriod(nextPeriod);
    }

    public void setPreviousPeriodWithoutChecks(OccupationPeriod previousPeriod) {
        if (previousPeriod != null && !previousPeriod.getPeriodInterval().isBefore(getPeriodInterval())) {
            throw new DomainException("error.occupationPeriod.invalid.previousPeriod");
        }
        super.setPreviousPeriod(previousPeriod);
    }

    @Override
    public void setNextPeriod(OccupationPeriod nextPeriod) {
        if (!allNestedPeriodsAreEmpty()) {
            throw new DomainException("error.occupationPeriod.previous.periods.not.empty");
        }
        this.setNextPeriodWithoutChecks(nextPeriod);
    }

    @Override
    public void setPreviousPeriod(OccupationPeriod previousPeriod) {
        if (!allNestedPeriodsAreEmpty()) {
            throw new DomainException("error.occupationPeriod.next.periods.not.empty");
        }
        this.setPreviousPeriodWithoutChecks(previousPeriod);
    }

    public Calendar getStartDate() {
        if (this.getStart() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStart());
            return result;
        }
        return null;
    }

    public void setEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnd(calendar.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setStartDate(Calendar calendar) {
        if (calendar != null) {
            this.setStart(calendar.getTime());
        } else {
            this.setStart(null);
        }
    }

    @Deprecated
    public void setStart(Date date) {
        Interval interval = this.getPeriodInterval();
        this.setPeriodInterval(IntervalTools.intervalWithStart(interval, date));
    }

    @Deprecated
    public void setEnd(Date date) {
        Interval interval = this.getPeriodInterval();
        this.setPeriodInterval(IntervalTools.intervalWithEnd(interval, date));
    }

    @Deprecated
    public Date getStart() {
        return this.getPeriodInterval().getStart().toDate();
    }

    @Deprecated
    public Date getEnd() {
        return this.getPeriodInterval().getEnd().toDate();
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.getEndDate();
        OccupationPeriod period = this.getNextPeriod();
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

    private boolean intersectPeriods(final Calendar start, final Calendar end) {
        return CalendarUtil.intersectDates(start, end, getStartDate(), getEndDate());
    }

    private boolean intersectPeriods(YearMonthDay start, YearMonthDay end) {
        return !getStartYearMonthDay().isAfter(end) && !getEndYearMonthDay().isBefore(start);
    }

    private boolean containsDay(YearMonthDay day) {
        return this.getPeriodInterval().contains(day.toDateTimeAtMidnight());
    }

    public void delete() {
        if (allNestedPeriodsAreEmpty()) {
            OccupationPeriod first = getFirstOccupationPeriodOfNestedPeriods();
            first.deleteAllNestedPeriods();
        }
    }

    private void deleteAllNestedPeriods() {
        OccupationPeriod nextPeriod = getNextPeriod();

        super.setNextPeriod(null);
        super.setPreviousPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();

        if (nextPeriod != null) {
            nextPeriod.delete();
        }
    }

    public boolean allNestedPeriodsAreEmpty() {
        OccupationPeriod firstOccupationPeriod = getFirstOccupationPeriodOfNestedPeriods();
        if (!firstOccupationPeriod.isEmpty()) {
            return false;
        }
        while (firstOccupationPeriod.getNextPeriod() != null) {
            if (!firstOccupationPeriod.getNextPeriod().isEmpty()) {
                return false;
            }
            firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
        }
        return true;
    }

    private boolean isEmpty() {
        return getLessons().isEmpty() && getExecutionDegrees().isEmpty();
    }

    public OccupationPeriod getLastOccupationPeriodOfNestedPeriods() {
        OccupationPeriod occupationPeriod = this;
        while (occupationPeriod.getNextPeriod() != null) {
            occupationPeriod = occupationPeriod.getNextPeriod();
        }
        return occupationPeriod;
    }

    private OccupationPeriod getFirstOccupationPeriodOfNestedPeriods() {
        OccupationPeriod occupationPeriod = this;
        while (occupationPeriod.getPreviousPeriod() != null) {
            occupationPeriod = occupationPeriod.getPreviousPeriod();
        }
        return occupationPeriod;
    }

    public static OccupationPeriod readByDates(Date startDate, Date endDate) {
        for (OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
                    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getStart(), startDate)
                    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getEnd(), endDate)) {
                return occupationPeriod;
            }
        }
        return null;
    }

    /**
     * 
     * @param startDate
     * @param endDate
     * @param startDatePart2
     * @param endDatePart2
     * @return
     */
    public static OccupationPeriod getOccupationPeriod(final Calendar startDate, final Calendar endDate,
            final Calendar startDatePart2, final Calendar endDatePart2) {
        OccupationPeriod occupationPeriod =
                OccupationPeriod.readOccupationPeriod(YearMonthDay.fromCalendarFields(startDate),
                        YearMonthDay.fromCalendarFields(endDate), YearMonthDay.fromCalendarFields(startDatePart2),
                        YearMonthDay.fromCalendarFields(endDatePart2));
        if (occupationPeriod == null) {
            final OccupationPeriod next =
                    startDatePart2 == null ? null : new OccupationPeriod(startDatePart2.getTime(), endDatePart2.getTime());
            occupationPeriod = new OccupationPeriod(startDate.getTime(), endDate.getTime());
            occupationPeriod.setNextPeriod(next);
        }
        return occupationPeriod;
    }

    public static OccupationPeriod getOccupationPeriod(final YearMonthDay startDate, final YearMonthDay endDate,
            final YearMonthDay startDatePart2, final YearMonthDay endDatePart2) {
        OccupationPeriod occupationPeriod =
                OccupationPeriod.readOccupationPeriod(startDate, endDate, startDatePart2, endDatePart2);
        if (occupationPeriod == null) {
            final OccupationPeriod next = startDatePart2 == null ? null : new OccupationPeriod(startDatePart2, endDatePart2);
            occupationPeriod = new OccupationPeriod(startDate, endDate);
            occupationPeriod.setNextPeriod(next);
        }
        return occupationPeriod;
    }

    /**
     * Created because semantics of readOccupationPeriod is not well defined but
     * we can't touch it because they're afraid of consequences.
     * 
     * @param startDate
     * @param endDate
     * @param startDatePart2
     * @param endDatePart2
     * @return
     */
    public static OccupationPeriod getEqualOccupationPeriod(final YearMonthDay startDate, final YearMonthDay endDate,
            final YearMonthDay startDatePart2, final YearMonthDay endDatePart2) {
        OccupationPeriod occupationPeriod =
                OccupationPeriod.readEqualOccupationPeriod(startDate, endDate, startDatePart2, endDatePart2);
        if (occupationPeriod == null) {
            final OccupationPeriod next = startDatePart2 == null ? null : new OccupationPeriod(startDatePart2, endDatePart2);
            occupationPeriod = new OccupationPeriod(startDate, endDate);
            occupationPeriod.setNextPeriod(next);
        }
        return occupationPeriod;
    }

    public static OccupationPeriod readOccupationPeriod(YearMonthDay start, YearMonthDay end) {
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
                    && occupationPeriod.getStartYearMonthDay().equals(start) && occupationPeriod.getEndYearMonthDay().equals(end)) {
                return occupationPeriod;
            }
        }
        return null;
    }

    public static OccupationPeriod readEqualOccupationPeriod(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2,
            final YearMonthDay endPart2) {
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.isEqualTo(start, end, startPart2, endPart2)) {
                return occupationPeriod;
            }
        }
        return null;
    }

    public static OccupationPeriod readOccupationPeriod(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2,
            final YearMonthDay endPart2) {
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getNextPeriod() == null
                    && occupationPeriod.getPreviousPeriod() == null
                    && occupationPeriod.getStartYearMonthDay().equals(start)
                    && occupationPeriod.getEndYearMonthDay().equals(end)
                    && ((!occupationPeriod.hasNextPeriod() && startPart2 == null) || (occupationPeriod.getNextPeriod()
                            .getStartYearMonthDay().equals(startPart2) && occupationPeriod.getNextPeriod().getEndYearMonthDay()
                            .equals(endPart2)))) {
                return occupationPeriod;
            }
        }
        return null;
    }

    public static OccupationPeriod readOccupationPeriod(final ExecutionCourse executionCourse, final YearMonthDay start,
            final YearMonthDay end) {
        OccupationPeriod result = null;
        boolean ok = true;
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionCourse.getExecutionYear() == executionDegree.getExecutionYear()) {
                    final OccupationPeriod occupationPeriod =
                            executionDegree.getPeriodLessons(executionCourse.getExecutionPeriod());
                    if (result == null) {
                        result = occupationPeriod;
                    } else if (result != occupationPeriod) {
                        ok = false;
                    }
                }
            }
        }
        if (ok && result != null) {
            return result;
        }
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
                    && occupationPeriod.getStartYearMonthDay().equals(start) && occupationPeriod.getEndYearMonthDay().equals(end)) {
                return occupationPeriod;
            }
        }
        return null;
    }

    public static OccupationPeriod createOccupationPeriodForLesson(final ExecutionCourse executionCourse,
            final YearMonthDay beginDate, final YearMonthDay endDate) {
        OccupationPeriod result = null;
        boolean ok = true;
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionCourse.getExecutionYear() == executionDegree.getExecutionYear()) {
                    final OccupationPeriod occupationPeriod =
                            executionDegree.getPeriodLessons(executionCourse.getExecutionPeriod());
                    if (result == null) {
                        result = occupationPeriod;
                    } else if (!result.isEqualTo(occupationPeriod)) {
                        ok = false;
                    }
                }
            }
        }
        if (ok && result != null) {
            if (result.getStartYearMonthDay().equals(beginDate) && result.getEndYearMonthDayWithNextPeriods().equals(endDate)) {
                return result;
            }
            return createNewPeriodWithExclusions(beginDate, endDate, result);
        }
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getNextPeriod() == null && occupationPeriod.getPreviousPeriod() == null
                    && occupationPeriod.getStartYearMonthDay().equals(beginDate)
                    && occupationPeriod.getEndYearMonthDay().equals(endDate)) {
                return occupationPeriod;
            }
        }
        return new OccupationPeriod(beginDate, endDate);
    }

    private static OccupationPeriod createNewPeriodWithExclusions(final YearMonthDay beginDate, final YearMonthDay endDate,
            final OccupationPeriod result) {
        final SortedSet<YearMonthDay> dates = new TreeSet<YearMonthDay>();

        dates.add(beginDate);
        dates.add(endDate);

        OccupationPeriod pop = result;
        for (OccupationPeriod nop = result.getNextPeriod(); nop != null; pop = nop, nop = nop.getNextPeriod()) {
            if (pop.getEndYearMonthDay().isAfter(beginDate) && pop.getEndYearMonthDay().isBefore(endDate)) {
                dates.add(pop.getEndYearMonthDay());
            }
            if (nop.getStartYearMonthDay().isAfter(beginDate) && nop.getStartYearMonthDay().isBefore(endDate)) {
                dates.add(nop.getStartYearMonthDay());
            }
        }

        return new OccupationPeriod(dates.toArray(new YearMonthDay[0]));
    }

    public boolean nestedOccupationPeriodsIntersectDates(Calendar start, Calendar end) {
        OccupationPeriod firstOccupationPeriod = this;
        while (firstOccupationPeriod != null) {
            if (firstOccupationPeriod.intersectPeriods(start, end)) {
                return true;
            }
            firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
        }
        return false;
    }

    public boolean nestedOccupationPeriodsIntersectDates(YearMonthDay start, YearMonthDay end) {
        OccupationPeriod firstOccupationPeriod = this;
        while (firstOccupationPeriod != null) {
            if (firstOccupationPeriod.intersectPeriods(start, end)) {
                return true;
            }
            firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
        }
        return false;
    }

    public boolean nestedOccupationPeriodsContainsDay(YearMonthDay day) {
        OccupationPeriod firstOccupationPeriod = this;
        while (firstOccupationPeriod != null) {
            if (firstOccupationPeriod.containsDay(day)) {
                return true;
            }
            firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
        }
        return false;
    }

    public boolean isGreater(OccupationPeriod period) {
        return this.getPeriodInterval().toDuration().isLongerThan(period.getPeriodInterval().toDuration());
    }

    public boolean isEqualTo(OccupationPeriod period) {
        if (hasNextPeriod() && period.hasNextPeriod()) {
            return isEqualTo(period.getStartYearMonthDay(), period.getEndYearMonthDay(), period.getNextPeriod()
                    .getStartYearMonthDay(), period.getNextPeriod().getEndYearMonthDay());
        }
        return getPeriodInterval().equals(period.getPeriodInterval());
    }

    public boolean isEqualTo(YearMonthDay start, YearMonthDay end, final YearMonthDay startPart2, final YearMonthDay endPart2) {
        final boolean eqStart = getStartYearMonthDay().equals(start);
        final boolean eqEnd = getEndYearMonthDay().equals(end);
        final boolean eqNextPeriod =
                hasNextPeriod() ? (getNextPeriod().getStartYearMonthDay().equals(startPart2)
                        && getNextPeriod().getEndYearMonthDay().equals(endPart2) ? true : false) : true;
        return eqStart && eqEnd && eqNextPeriod;
    }

    public YearMonthDay getEndYearMonthDayWithNextPeriods() {
        return hasNextPeriod() ? getNextPeriod().getEndYearMonthDayWithNextPeriods() : getEndYearMonthDay();
    }

    public String asString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("[%s,%s]", sdf.format(getStartDate().getTime()), sdf.format(getEndDate().getTime()));
    }

    public Interval getIntervalWithNextPeriods() {
        return new Interval(getStartYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(), getEndYearMonthDayWithNextPeriods()
                .toLocalDate().toDateTimeAtStartOfDay());
    }

    public static OccupationPeriod getOccupationPeriod(final YearMonthDay[] yearMonthDays) {
        for (final OccupationPeriod occupationPeriod : Bennu.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.matches(yearMonthDays)) {
                return occupationPeriod;
            }
        }
        return null;
    }

    public boolean matches(final YearMonthDay[] yearMonthDays) {
        if (yearMonthDays == null || yearMonthDays.length < 2) {
            return false;
        }
        final YearMonthDay start = yearMonthDays[0];
        final YearMonthDay end = yearMonthDays[1];
        if (start == null || !start.equals(getStartYearMonthDay()) || end == null || !end.equals(getEndYearMonthDay())) {
            return false;
        }
        if (yearMonthDays.length > 2) {
            final int l = yearMonthDays.length;
            final YearMonthDay[] nextYearMonthDays = new YearMonthDay[l - 2];
            System.arraycopy(yearMonthDays, 2, nextYearMonthDays, 0, l - 2);
            return hasNextPeriod() && getNextPeriod().matches(nextYearMonthDays);
        }
        return !hasNextPeriod();
    }

    public YearMonthDay[] toYearMonthDays() {
        if (hasNextPeriod()) {
            final YearMonthDay[] nextValue = getNextPeriod().toYearMonthDays();
            final int l = nextValue.length;
            final YearMonthDay[] result = new YearMonthDay[l + 2];
            result[0] = getStartYearMonthDay();
            result[1] = getEndYearMonthDay();
            System.arraycopy(nextValue, 0, result, 2, l);
            return result;
        } else {
            return new YearMonthDay[] { getStartYearMonthDay(), getEndYearMonthDay() };
        }
    }

    /*
     * Deprecated getters and setters, meant exclusively for compatibility. New
     * clients of the Class should use the interval ones Instead.
     */

    @Deprecated
    public YearMonthDay getStartYearMonthDay() {
        Interval interval = this.getPeriodInterval();
        return IntervalTools.getStartYMD(interval);
    }

    @Deprecated
    public YearMonthDay getEndYearMonthDay() {
        Interval interval = this.getPeriodInterval();
        return IntervalTools.getEndYMD(interval);
    }

    @Deprecated
    public void setStartYearMonthDay(YearMonthDay start) {
        Interval interval = this.getPeriodInterval();
        this.setPeriodInterval(IntervalTools.intervalWithStart(interval, start));
    }

    @Deprecated
    public void setEndYearMonthDay(YearMonthDay end) {
        Interval interval = this.getPeriodInterval();
        this.setPeriodInterval(IntervalTools.intervalWithEnd(interval, end));
    }

    public List<Interval> getIntervals() {
        List<Interval> intervals = new LinkedList<Interval>();

        OccupationPeriod period = this;

        while (period != null) {
            intervals.add(period.getPeriodInterval());
            period = period.getNextPeriod();
        }

        return intervals;
    }

    public void editDates(Iterator<Interval> intervals) {

        this.setPeriodInterval(intervals.next());

        if (!intervals.hasNext()) {
            this.setNextPeriodWithoutChecks(null);
        } else {
            if (this.getNextPeriod() != null) {
                this.getNextPeriod().editDates(intervals);
            } else {
                this.setNextPeriodWithoutChecks(new OccupationPeriod(intervals));
            }
        }

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Lesson> getLessons() {
        return getLessonsSet();
    }

    @Deprecated
    public boolean hasAnyLessons() {
        return !getLessonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.OccupationPeriodReference> getExecutionDegrees() {
        return getExecutionDegreesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegrees() {
        return !getExecutionDegreesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNextPeriod() {
        return getNextPeriod() != null;
    }

    @Deprecated
    public boolean hasPeriodInterval() {
        return getPeriodInterval() != null;
    }

    @Deprecated
    public boolean hasPreviousPeriod() {
        return getPreviousPeriod() != null;
    }

}
