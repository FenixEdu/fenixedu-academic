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
package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public abstract class EventSpaceOccupation extends EventSpaceOccupation_Base {

    public static final Comparator<EventSpaceOccupation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("period.startDate"));
        ((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("externalId"));
    }

    private static int SATURDAY_IN_JODA_TIME = 6, SUNDAY_IN_JODA_TIME = 7;

    private static transient Locale locale = I18N.getLocale();

    public abstract Boolean getDailyFrequencyMarkSaturday();

    public abstract Boolean getDailyFrequencyMarkSunday();

    public abstract YearMonthDay getBeginDate();

    public abstract YearMonthDay getEndDate();

    public abstract HourMinuteSecond getStartTimeDateHourMinuteSecond();

    public abstract HourMinuteSecond getEndTimeDateHourMinuteSecond();

    public abstract DiaSemana getDayOfWeek();

    public abstract FrequencyType getFrequency();

    protected EventSpaceOccupation() {
        super();
    }

    public void setResource(Space resource) {
        if (!(SpaceUtils.isRoom(resource) || SpaceUtils.isRoomSubdivision(resource))) {
            throw new DomainException("error.EventSpaceOccupation.invalid.resource");
        }
        getSpaceSet().clear();
        super.addSpace(resource);
    }

    public Space getRoom() {
        return getSpace();
    }

    public Calendar getStartTime() {
        HourMinuteSecond hms = getStartTimeDateHourMinuteSecond();
        Date date =
                (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
        if (date != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(date);
            return result;
        }
        return null;
    }

    public Calendar getEndTime() {
        HourMinuteSecond hms = getEndTimeDateHourMinuteSecond();
        Date date =
                (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
        if (date != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(date);
            return result;
        }
        return null;
    }

    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return getBeginDate() != null && getEndDate() != null && !getBeginDate().isAfter(endDate)
                && !getEndDate().isBefore(startDate);
    }

    public boolean alreadyWasOccupiedBy(final EventSpaceOccupation occupation) {

        if (this.equals(occupation)) {
            return true;
        }

        if (occupation.isLessonInstanceSpaceOccupation() || occupation.isWrittenEvaluationSpaceOccupation()
                || intersects(occupation.getBeginDate(), occupation.getEndDate())) {

            List<Interval> thisOccupationIntervals =
                    getEventSpaceOccupationIntervals(occupation.getBeginDate(), occupation.getEndDate());
            List<Interval> passedOccupationIntervals =
                    occupation.getEventSpaceOccupationIntervals((YearMonthDay) null, (YearMonthDay) null);

            for (Interval interval : thisOccupationIntervals) {
                for (Interval passedInterval : passedOccupationIntervals) {
                    if (interval.getStart().isBefore(passedInterval.getEnd())
                            && interval.getEnd().isAfter(passedInterval.getStart())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isWrittenEvaluationSpaceOccupation() {
        return this instanceof WrittenEvaluationSpaceOccupation;
    }

    private boolean isLessonInstanceSpaceOccupation() {
        return this instanceof LessonInstanceSpaceOccupation;
    }

    public boolean alreadyWasOccupiedIn(final YearMonthDay startDate, final YearMonthDay endDate,
            final HourMinuteSecond startTime, final HourMinuteSecond endTime, final DiaSemana dayOfWeek,
            final FrequencyType frequency, final Boolean dailyFrequencyMarkSaturday, final Boolean dailyFrequencyMarkSunday) {

        startTime.setSecondOfMinute(0);
        endTime.setSecondOfMinute(0);

        if (intersects(startDate, endDate)) {

            List<Interval> thisOccupationIntervals = getEventSpaceOccupationIntervals(startDate, endDate);
            List<Interval> passedOccupationIntervals =
                    generateEventSpaceOccupationIntervals(startDate, endDate, startTime, endTime, frequency, dayOfWeek,
                            dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday, null, null);

            for (Interval interval : thisOccupationIntervals) {
                for (Interval passedInterval : passedOccupationIntervals) {
                    if (interval.getStart().isBefore(passedInterval.getEnd())
                            && interval.getEnd().isAfter(passedInterval.getStart())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean overlaps(final Interval[] intervals) {
        for (final Interval interval : intervals) {
            if (overlaps(interval)) {
                return true;
            }
//            final DateTime start = interval.getStart();
//            final DateTime end = interval.getEnd();
//            if (alreadyWasOccupiedIn(start.toYearMonthDay(), end.toYearMonthDay(), new HourMinuteSecond(start.toDate()),
//                    new HourMinuteSecond(end.toDate()), null, null, null, null)) {
//                return true;
//            }
        }
        return false;
    }

    protected abstract boolean overlaps(final Interval interval);

    public List<Interval> getEventSpaceOccupationIntervals(DateTime start, DateTime end) {
        final Interval i = new Interval(start, end);
        final List<Interval> intervals = getEventSpaceOccupationIntervals(start.toYearMonthDay(), end.toYearMonthDay());
        for (final Iterator<Interval> iterator = intervals.iterator(); iterator.hasNext();) {
            final Interval interval = iterator.next();
            if (!interval.overlaps(i)) {
                iterator.remove();
            }
        }
        return intervals;
    }

    @Override
    public List<Interval> getIntervals() {
        return getEventSpaceOccupationIntervals((YearMonthDay) null, (YearMonthDay) null);
    }

    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {
        return generateEventSpaceOccupationIntervals(getBeginDate(), getEndDate(), getStartTimeDateHourMinuteSecond(),
                getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(),
                getDailyFrequencyMarkSunday(), startDateToSearch, endDateToSearch);
    }

    public static List<Interval> generateEventSpaceOccupationIntervals(YearMonthDay begin, final YearMonthDay end,
            final HourMinuteSecond beginTime, final HourMinuteSecond endTime, final FrequencyType frequency,
            final DiaSemana diaSemana, final Boolean dailyFrequencyMarkSaturday, final Boolean dailyFrequencyMarkSunday,
            final YearMonthDay startDateToSearch, final YearMonthDay endDateToSearch) {

        List<Interval> result = new ArrayList<Interval>();
        begin = getBeginDateInSpecificWeekDay(diaSemana, begin);

        if (frequency == null) {
            if (!begin.isAfter(end)
                    && (startDateToSearch == null || (!end.isBefore(startDateToSearch) && !begin.isAfter(endDateToSearch)))) {
                result.add(createNewInterval(begin, end, beginTime, endTime));
                return result;
            }
        } else {
            int numberOfDaysToSum = frequency.getNumberOfDays();
            while (true) {
                if (begin.isAfter(end)) {
                    break;
                }
                if (startDateToSearch == null || (!begin.isBefore(startDateToSearch) && !begin.isAfter(endDateToSearch))) {

                    Interval interval = createNewInterval(begin, begin, beginTime, endTime);

                    if (!frequency.equals(FrequencyType.DAILY)
                            || ((dailyFrequencyMarkSaturday || interval.getStart().getDayOfWeek() != SATURDAY_IN_JODA_TIME) && (dailyFrequencyMarkSunday || interval
                                    .getStart().getDayOfWeek() != SUNDAY_IN_JODA_TIME))) {

                        result.add(interval);
                    }
                }
                begin = begin.plusDays(numberOfDaysToSum);
            }
        }
        return result;
    }

    protected DateTime getInstant(boolean firstInstant, YearMonthDay begin, final YearMonthDay end,
            final HourMinuteSecond beginTime, final HourMinuteSecond endTime, final FrequencyType frequency,
            final DiaSemana diaSemana, final Boolean dailyFrequencyMarkSaturday, final Boolean dailyFrequencyMarkSunday) {

        DateTime instantResult = null;
        begin = getBeginDateInSpecificWeekDay(diaSemana, begin);

        if (frequency == null) {
            if (!begin.isAfter(end)) {
                if (firstInstant) {
                    return begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0));
                } else {
                    return end.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0));
                }
            }
        } else {
            int numberOfDaysToSum = frequency.getNumberOfDays();
            while (true) {
                if (begin.isAfter(end)) {
                    break;
                }

                DateTime intervalEnd = begin.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0));
                if (!frequency.equals(FrequencyType.DAILY)
                        || ((dailyFrequencyMarkSaturday || intervalEnd.getDayOfWeek() != SATURDAY_IN_JODA_TIME) && (dailyFrequencyMarkSunday || intervalEnd
                                .getDayOfWeek() != SUNDAY_IN_JODA_TIME))) {

                    if (firstInstant) {
                        return begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0));
                    } else {
                        instantResult = intervalEnd;
                    }
                }
                begin = begin.plusDays(numberOfDaysToSum);
            }
        }
        return instantResult;
    }

    private static YearMonthDay getBeginDateInSpecificWeekDay(DiaSemana diaSemana, YearMonthDay begin) {
        if (diaSemana != null) {
            YearMonthDay newBegin =
                    begin.toDateTimeAtMidnight().withDayOfWeek(diaSemana.getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();
            if (newBegin.isBefore(begin)) {
                begin = newBegin.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);
            } else {
                begin = newBegin;
            }
        }
        return begin;
    }

    protected static Interval createNewInterval(YearMonthDay begin, YearMonthDay end, HourMinuteSecond beginTime,
            HourMinuteSecond endTime) {
        return new Interval(begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0)),
                end.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0)));
    }

    public DateTime getFirstInstant() {
        return getInstant(true, getBeginDate(), getEndDate(), getStartTimeDateHourMinuteSecond(),
                getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(),
                getDailyFrequencyMarkSunday());
    }

    public DateTime getLastInstant() {
        return getInstant(false, getBeginDate(), getEndDate(), getStartTimeDateHourMinuteSecond(),
                getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(),
                getDailyFrequencyMarkSunday());
    }

    public String getPrettyPrint() {
        StringBuilder builder = new StringBuilder();
        if (getFrequency() == null) {
            builder.append(getBeginDate().toString("dd/MM/yyyy")).append(" ").append(getPresentationBeginTime());
            builder.append(" - ").append(getEndDate().toString("dd/MM/yyyy")).append(" ").append(getPresentationEndTime());
        } else {
            builder.append(getBeginDate().toString("dd/MM/yyyy")).append(" - ").append(getEndDate().toString("dd/MM/yyyy"));
            builder.append(" (").append(getPresentationBeginTime()).append(" - ").append(getPresentationEndTime()).append(")");
        }
        return builder.toString();
    }

    @Override
    public String getSubject() {
        return getPresentationString();
    }

    public String getPresentationString() {
        return StringUtils.EMPTY;
    }

    public String getPresentationBeginTime() {
        return getStartTimeDateHourMinuteSecond().toString("HH:mm");
    }

    public String getPresentationEndTime() {
        return getEndTimeDateHourMinuteSecond().toString("HH:mm");
    }

    public String getPresentationBeginDate() {
        return getBeginDate().toString("dd MMMM yyyy", locale) + " ("
                + getBeginDate().toDateTimeAtMidnight().toString("E", locale) + ")";
    }

    public String getPresentationEndDate() {
        return getEndDate().toString("dd MMMM yyyy", locale) + " (" + getEndDate().toDateTimeAtMidnight().toString("E", locale)
                + ")";
    }

    public abstract boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start,
            final DateTime end);

    @Override
    public void delete() {
        setBennu(null);
        getSpaceSet().clear();
        super.deleteDomainObject();
    }
}
