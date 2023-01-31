/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.util.icalendar.ClassEventBean;
import org.fenixedu.academic.domain.util.icalendar.EventBean;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Weeks;
import org.joda.time.YearMonthDay;

public class Lesson extends Lesson_Base {

    public static int NUMBER_OF_MINUTES_IN_HOUR = 60;
    public static int NUMBER_OF_DAYS_IN_WEEK = 7;

    public static final Comparator<Lesson> LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME = new Comparator<Lesson>() {

        @Override
        public int compare(Lesson o1, Lesson o2) {
            final int cd = o1.getDiaSemana().getDiaSemana().compareTo(o2.getDiaSemana().getDiaSemana());
            if (cd != 0) {
                return cd;
            }
            final int cb = o1.getBeginHourMinuteSecond().compareTo(o2.getBeginHourMinuteSecond());
            if (cb != 0) {
                return cb;
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }

    };

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, Shift shift, FrequencyType frequency,
            ExecutionInterval executionInterval, OccupationPeriod period, Space room) {

        super();

        if (shift != null) {
            final Interval maxLessonsInterval = shift.getExecutionCourse().getMaxLessonsInterval();
            if (period == null || period.getStartYearMonthDay().isBefore(maxLessonsInterval.getStart().toLocalDate())) {
                throw new DomainException("error.Lesson.invalid.begin.date");
            }
            if (period.getEndYearMonthDayWithNextPeriods().isAfter(maxLessonsInterval.getEnd().toLocalDate())) {
                throw new DomainException("error.invalid.new.date");
            }
        }

        setRootDomainObject(Bennu.getInstance());
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setShift(shift);
        setFrequency(frequency);
        setPeriod(period);

        checkShiftLoad(shift);

        if (room != null) {
            new LessonSpaceOccupation(room, this);
        }
    }

    public void edit(final Space newRoom) {
        lessonSpaceOccupationManagement(newRoom);
    }

    public void delete() {
        if (hasAnyAssociatedSummaries()) {
            throw new DomainException("error.deleteLesson.with.summaries", prettyPrint());
        }

        final OccupationPeriod period = getPeriod();
        super.setPeriod(null);
        if (period != null) {
            period.delete();
        }

        if (getLessonSpaceOccupation() != null) {
            getLessonSpaceOccupation().delete();
        }

        while (hasAnyLessonInstances()) {
            getLessonInstancesSet().iterator().next().delete();
        }

        super.setShift(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getFrequency() != null && getDiaSemana() != null;
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkTimeInterval() {
        final HourMinuteSecond start = getBeginHourMinuteSecond();
        final HourMinuteSecond end = getEndHourMinuteSecond();
        return start != null && end != null && start.isBefore(end);
    }

    private void lessonSpaceOccupationManagement(Space newRoom) {
        LessonSpaceOccupation lessonSpaceOccupation = getLessonSpaceOccupation();
        if (newRoom != null) {
            if (!wasFinished()) {
                if (lessonSpaceOccupation == null) {
                    lessonSpaceOccupation = new LessonSpaceOccupation(newRoom, this);
                } else {
                    lessonSpaceOccupation.edit(newRoom);
                }
            }
        } else {
            if (lessonSpaceOccupation != null) {
                lessonSpaceOccupation.delete();
            }
        }
        for (final LessonInstance lessonInstance : getLessonInstancesSet()) {
            if (lessonInstance.getDay().isAfter(new LocalDate())) {
                if (newRoom == null) {
                    lessonInstance.setLessonInstanceSpaceOccupation(null);
                } else {
                    LessonInstanceSpaceOccupation.findOccupationForLessonAndSpace(this, newRoom).ifPresentOrElse(
                            o -> o.add(lessonInstance), () -> new LessonInstanceSpaceOccupation(newRoom, lessonInstance));
                }
            }
        }
    }

    @Override
    public void setShift(Shift shift) {
        if (shift == null) {
            throw new DomainException("error.Lesson.empty.shift");
        }
        super.setShift(shift);
    }

    @Override
    public void setFrequency(FrequencyType frequency) {
        if (frequency == null) {
            throw new DomainException("error.Lesson.empty.type");
        }
        super.setFrequency(frequency);
    }

    @Override
    public void setPeriod(OccupationPeriod period) {
        if (period == null) {
            throw new DomainException("error.Lesson.empty.period");
        }
        super.setPeriod(period);
    }

    public boolean wasFinished() {
        return getPeriod() == null;
    }

    public ExecutionCourse getExecutionCourse() {
        return getShift().getExecutionCourse();
    }

    public ExecutionInterval getExecutionPeriod() {
        return getShift().getExecutionPeriod();
    }

    @Deprecated
    public Space getSala() {
        if (getLessonSpaceOccupation() != null) {
            return getLessonSpaceOccupation().getSpace();
        } else if (hasAnyLessonInstances() && wasFinished()) {
            return getLastLessonInstance().getRoom();
        }
        return null;
    }

    @Deprecated
    public boolean hasSala() {
        return getSala() != null;
    }

    public Stream<Space> getSpaces() {
        final LessonSpaceOccupation spaceOccupation = getLessonSpaceOccupation();
        if (spaceOccupation != null) {
            return spaceOccupation.getSpaces().stream();
        }

        return getLessonInstancesSet().stream().flatMap(LessonInstance::getSpaces);
    }

    public void refreshPeriodAndInstancesInSummaryCreation(YearMonthDay newBeginDate) {
        if (!wasFinished() && newBeginDate != null && newBeginDate.isAfter(getPeriod().getStartYearMonthDay())) {
            SortedSet<YearMonthDay> instanceDates =
                    getAllLessonInstancesDatesToCreate(getLessonStartDay(), newBeginDate.minusDays(1), true);
            YearMonthDay newEndDate = getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay();
            if (!newBeginDate.isAfter(newEndDate)) {
                refreshPeriod(newBeginDate, getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay());
            } else {
                OccupationPeriod period = getPeriod();
                removeLessonSpaceOccupationAndPeriod();
                period.delete();
            }
            createAllLessonInstances(instanceDates);
        }
    }

    private void createAllLessonInstances(SortedSet<YearMonthDay> instanceDates) {
        for (YearMonthDay day : instanceDates) {
            new LessonInstance(this, day);
        }
    }

    private SortedSet<YearMonthDay> getAllLessonInstancesDatesToCreate(YearMonthDay startDate, YearMonthDay endDate,
            Boolean createLessonInstances) {

        if (startDate != null && endDate != null && !startDate.isAfter(endDate) && createLessonInstances) {

            SortedSet<YearMonthDay> possibleLessonDates = getAllValidLessonDatesWithoutInstancesDates(startDate, endDate);
            List<LessonInstance> allLessonInstancesUntil = getAllLessonInstancesUntil(endDate.toLocalDate());

            for (LessonInstance lessonInstance : allLessonInstancesUntil) {
                possibleLessonDates.remove(lessonInstance.getDay());
            }

            return possibleLessonDates;
        }
        return new TreeSet<YearMonthDay>();
    }

    private void refreshPeriod(YearMonthDay newBeginDate, YearMonthDay newEndDate) {

        if (newBeginDate != null && newEndDate != null && !newBeginDate.isAfter(newEndDate)) {

            boolean newPeriod = false;
            OccupationPeriod currentPeriod = getPeriod();
            OccupationPeriod oldFirstPeriod = currentPeriod;

            if (currentPeriod == null || currentPeriod.getNextPeriod() == null) {
                setPeriod(new OccupationPeriod(newBeginDate, newEndDate));
                newPeriod = true;

            } else {

                while (currentPeriod != null) {

                    if (currentPeriod.getStartYearMonthDay().isAfter(newEndDate)) {
                        newPeriod = false;
                        break;
                    }

                    if (!currentPeriod.getEndYearMonthDay().isBefore(newBeginDate)) {

                        if (!currentPeriod.getStartYearMonthDay().isAfter(newBeginDate)) {
                            setPeriod(getNewNestedPeriods(currentPeriod, newBeginDate, newEndDate));

                        } else {
                            if (currentPeriod.equals(oldFirstPeriod)) {
                                setPeriod(getNewNestedPeriods(currentPeriod, newBeginDate, newEndDate));
                            } else {
                                setPeriod(getNewNestedPeriods(currentPeriod, currentPeriod.getStartYearMonthDay(), newEndDate));
                            }
                        }

                        newPeriod = true;
                        break;
                    }
                    currentPeriod = currentPeriod.getNextPeriod();
                }
            }

            if (!newPeriod) {
                removeLessonSpaceOccupationAndPeriod();
            }

            if (oldFirstPeriod != null) {
                oldFirstPeriod.delete();
            }
        }
    }

    private OccupationPeriod getNewNestedPeriods(OccupationPeriod currentPeriod, YearMonthDay newBeginDate,
            YearMonthDay newEndDate) {

        if (!currentPeriod.getEndYearMonthDay().isBefore(newEndDate)) {
            return new OccupationPeriod(newBeginDate, newEndDate);

        } else {

            OccupationPeriod newPeriod = new OccupationPeriod(newBeginDate, currentPeriod.getEndYearMonthDay());
            OccupationPeriod newPeriodPointer = newPeriod;

            while (currentPeriod.getNextPeriod() != null) {

                if (currentPeriod.getNextPeriod().getStartYearMonthDay().isAfter(newEndDate)) {
                    break;
                }

                if (!currentPeriod.getNextPeriod().getEndYearMonthDay().isBefore(newEndDate)) {
                    OccupationPeriod newNextPeriod =
                            new OccupationPeriod(currentPeriod.getNextPeriod().getStartYearMonthDay(), newEndDate);
                    newPeriodPointer.setNextPeriod(newNextPeriod);
                    break;

                } else {
                    OccupationPeriod newNextPeriod = new OccupationPeriod(currentPeriod.getNextPeriod().getStartYearMonthDay(),
                            currentPeriod.getNextPeriod().getEndYearMonthDay());
                    newPeriodPointer.setNextPeriod(newNextPeriod);
                    newPeriodPointer = newNextPeriod;
                    currentPeriod = currentPeriod.getNextPeriod();
                }
            }

            return newPeriod;
        }
    }

    private void removeLessonSpaceOccupationAndPeriod() {
        if (getLessonSpaceOccupation() != null) {
            getLessonSpaceOccupation().delete();
        }
        super.setPeriod(null);
    }

    public void removeOccupationPeriod() {
        super.setPeriod(null);
    }

    public LessonSpaceOccupation getRoomOccupation() {
        return getLessonSpaceOccupation();
    }

    private int getUnitMinutes() {
        return Minutes.minutesBetween(getBeginHourMinuteSecond(), getEndHourMinuteSecond()).getMinutes();
    }

    public BigDecimal getTotalHours() {
        return getUnitHours().multiply(BigDecimal.valueOf(getFinalNumberOfLessonInstances()));
    }

    public Duration getTotalDuration() {
        return Minutes.minutesBetween(getBeginHourMinuteSecond(), getEndHourMinuteSecond()).toStandardDuration();
    }

    public BigDecimal getUnitHours() {
        return BigDecimal.valueOf(getUnitMinutes()).divide(BigDecimal.valueOf(NUMBER_OF_MINUTES_IN_HOUR), 2,
                RoundingMode.HALF_UP);
    }

    public String getInicioString() {
        return String.valueOf(getInicio().get(Calendar.HOUR_OF_DAY));
    }

    public String getFimString() {
        return String.valueOf(getFim().get(Calendar.HOUR_OF_DAY));
    }

    public double hoursAfter(int hour) {

        HourMinuteSecond afterHour = new HourMinuteSecond(hour, 0, 0);

        if (!getBeginHourMinuteSecond().isBefore(afterHour)) {
            return getUnitHours().doubleValue();

        } else if (getEndHourMinuteSecond().isAfter(afterHour)) {
            return BigDecimal.valueOf(Minutes.minutesBetween(afterHour, getEndHourMinuteSecond()).getMinutes())
                    .divide(BigDecimal.valueOf(NUMBER_OF_MINUTES_IN_HOUR), 2, RoundingMode.HALF_UP).doubleValue();
        }

        return 0.0;
    }

    public Summary getSummaryByDate(YearMonthDay date) {
        for (Summary summary : getAssociatedSummaries()) {
            if (summary.getSummaryDateYearMonthDay().isEqual(date)) {
                return summary;
            }
        }
        return null;
    }

    public List<Summary> getAssociatedSummaries() {
        List<Summary> result = new ArrayList<Summary>();
        Collection<LessonInstance> lessonInstances = getLessonInstancesSet();
        for (LessonInstance lessonInstance : lessonInstances) {
            if (lessonInstance.getSummary() != null) {
                result.add(lessonInstance.getSummary());
            }
        }
        return result;
    }

    public boolean hasAnyAssociatedSummaries() {
        return !getAssociatedSummaries().isEmpty();
    }

    public SortedSet<Summary> getSummariesSortedByDate() {
        return getSummaries(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
    }

    public SortedSet<Summary> getSummariesSortedByNewestDate() {
        return getSummaries(Summary.COMPARATOR_BY_DATE_AND_HOUR);
    }

    public boolean isTimeValidToInsertSummary(HourMinuteSecond timeToInsert, YearMonthDay summaryDate) {

        YearMonthDay currentDate = new YearMonthDay();
        if (timeToInsert == null || summaryDate == null || summaryDate.isAfter(currentDate)) {
            return false;
        }

        if (currentDate.isEqual(summaryDate)) {
            HourMinuteSecond lessonStartTime = null;
            LessonInstance lessonInstance = getLessonInstanceFor(summaryDate);
            lessonStartTime = lessonInstance != null ? lessonInstance.getStartTime() : getBeginHourMinuteSecond();
            return !lessonStartTime.isAfter(timeToInsert);
        }

        return true;
    }

    public boolean isDateValidToInsertSummary(YearMonthDay date) {
        YearMonthDay currentDate = new YearMonthDay();
        SortedSet<YearMonthDay> allLessonDatesEvenToday = getAllLessonDatesUntil(currentDate);
        return (allLessonDatesEvenToday.isEmpty() || date == null) ? false : allLessonDatesEvenToday.contains(date);
    }

    public boolean isDateValidToInsertExtraSummary(YearMonthDay date) {
        return !(getLessonStartDay().isAfter(date) || getLessonEndDay().isBefore(date));
    }

    private YearMonthDay getLessonStartDay() {
        if (!wasFinished()) {
            YearMonthDay periodBegin = getPeriod().getStartYearMonthDay();
            return getValidBeginDate(periodBegin);
        }
        return null;
    }

    private YearMonthDay getLessonEndDay() {
        if (!wasFinished()) {
            YearMonthDay periodEnd = getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay();
            return getValidEndDate(periodEnd);
        }
        return null;
    }

    private YearMonthDay getValidBeginDate(YearMonthDay startDate) {
        final YearMonthDay periodEndDate = getPeriod() != null ? getPeriod().getEndYearMonthDayWithNextPeriods() : null;
        if (periodEndDate != null) {

            YearMonthDay lessonBegin = startDate.toDateTimeAtMidnight()
                    .withDayOfWeek(getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();
            if (lessonBegin.isBefore(startDate)) {
                lessonBegin = lessonBegin.plusDays(NUMBER_OF_DAYS_IN_WEEK);
            }

            while (!isDayValid(lessonBegin, null)) {
                if (!lessonBegin.isAfter(periodEndDate)) {
                    lessonBegin = lessonBegin.plusDays(NUMBER_OF_DAYS_IN_WEEK);
                } else {
                    return null;
                }
            }

            return lessonBegin;
        }

        return null;
    }

    private YearMonthDay getValidEndDate(YearMonthDay endDate) {
        YearMonthDay lessonEnd =
                endDate.toDateTimeAtMidnight().withDayOfWeek(getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();
        if (lessonEnd.isAfter(endDate)) {
            lessonEnd = lessonEnd.minusDays(NUMBER_OF_DAYS_IN_WEEK);
        }
        return lessonEnd;
    }

    public Space getLessonCampus() {
        if (!wasFinished()) {
            return hasSala() ? SpaceUtils.getSpaceCampus(getSala()) : null;
        } else {
            LessonInstance lastLessonInstance = getLastLessonInstance();
            if (lastLessonInstance != null && lastLessonInstance.getRoom() != null) {
                return SpaceUtils.getSpaceCampus(lastLessonInstance.getRoom());
            } else {
                return null;
            }
        }
    }

    public YearMonthDay getNextPossibleSummaryDate() {

        YearMonthDay currentDate = new YearMonthDay();
        HourMinuteSecond now = new HourMinuteSecond();
        Summary lastSummary = getLastSummary();

        if (lastSummary != null) {

            SortedSet<YearMonthDay> datesEvenToday = getAllLessonDatesUntil(currentDate);
            SortedSet<YearMonthDay> possibleDates = datesEvenToday.tailSet(lastSummary.getSummaryDateYearMonthDay());

            possibleDates.remove(lastSummary.getSummaryDateYearMonthDay());
            if (!possibleDates.isEmpty()) {
                YearMonthDay nextPossibleDate = possibleDates.first();
                return isTimeValidToInsertSummary(now, nextPossibleDate) ? nextPossibleDate : null;
            }

        } else {
            YearMonthDay nextPossibleDate;
            if (hasAnyLessonInstances()) {
                nextPossibleDate = getFirstLessonInstance().getDay();
            } else {
                SortedSet<YearMonthDay> validLessonDates =
                        getAllValidLessonDatesWithoutInstancesDates(getLessonStartDay(), getLessonEndDay());
                nextPossibleDate = validLessonDates.size() > 0 ? validLessonDates.first() : null;
            }
            return isTimeValidToInsertSummary(now, nextPossibleDate) ? nextPossibleDate : null;
        }

        return null;
    }

    public SortedSet<YearMonthDay> getAllPossibleDatesToInsertSummary() {

        HourMinuteSecond now = new HourMinuteSecond();
        YearMonthDay currentDate = new YearMonthDay();
        SortedSet<YearMonthDay> datesToInsert = getAllLessonDatesUntil(currentDate);

        for (Summary summary : getAssociatedSummaries()) {
            YearMonthDay summaryDate = summary.getSummaryDateYearMonthDay();
            datesToInsert.remove(summaryDate);
        }

        for (Iterator<YearMonthDay> iter = datesToInsert.iterator(); iter.hasNext();) {
            YearMonthDay date = iter.next();
            if (!isTimeValidToInsertSummary(now, date)) {
                iter.remove();
            }
        }

        return datesToInsert;
    }

    public SortedSet<YearMonthDay> getAllLessonDatesWithoutInstanceDates() {
        SortedSet<YearMonthDay> dates = new TreeSet<YearMonthDay>();
        if (!wasFinished()) {
            YearMonthDay startDateToSearch = getLessonStartDay();
            YearMonthDay endDateToSearch = getLessonEndDay();
            dates.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
        }
        return dates;
    }

    public SortedSet<Interval> getAllLessonIntervalsWithoutInstanceDates() {
        SortedSet<Interval> dates = new TreeSet<Interval>(new Comparator<Interval>() {

            @Override
            public int compare(Interval o1, Interval o2) {
                return o1.getStart().compareTo(o2.getStart());
            }

        });
        if (!wasFinished()) {
            YearMonthDay startDateToSearch = getLessonStartDay();
            YearMonthDay endDateToSearch = getLessonEndDay();
            final HourMinuteSecond b = getBeginHourMinuteSecond();
            final HourMinuteSecond e = getEndHourMinuteSecond();
            for (final YearMonthDay yearMonthDay : getAllValidLessonDatesWithoutInstancesDates(startDateToSearch,
                    endDateToSearch)) {
                dates.add(new Interval(toDateTime(yearMonthDay, b), toDateTime(yearMonthDay, e)));
            }
        }
        return dates;
    }

//    public boolean overlaps(final Interval interval) {
//        if (wasFinished()) {
//            return false;
//        }
//        final YearMonthDay startDateToSearch = getLessonStartDay();
//        if (startDateToSearch == null) {
//            return false;
//        }
//        final YearMonthDay endDateToSearch = getLessonEndDay();
//        if (endDateToSearch == null) {
//            return false;
//        }
//        final DateTime intervalStart = interval.getStart();
//        if (intervalStart.isAfter(endDateToSearch.toDateTimeAtMidnight().plusDays(1))) {
//            return false;
//        }
//        final DateTime intervalEnd = interval.getEnd();
//        if (intervalEnd.isBefore(startDateToSearch.toDateTimeAtMidnight())) {
//            return false;
//        }
//        final HourMinuteSecond b = getBeginHourMinuteSecond();
//        final HourMinuteSecond e = getEndHourMinuteSecond();
//        for (final YearMonthDay yearMonthDay : getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch)) {
//            if (new Interval(toDateTime(yearMonthDay, b), toDateTime(yearMonthDay, e)).overlaps(interval)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public SortedSet<YearMonthDay> getAllLessonDates() {
        SortedSet<YearMonthDay> dates = getAllLessonInstanceDates();
        if (!wasFinished()) {
            YearMonthDay startDateToSearch = getLessonStartDay();
            YearMonthDay endDateToSearch = getLessonEndDay();
            dates.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
        }
        return dates;
    }

    public int getFinalNumberOfLessonInstances() {
        int count = getLessonInstancesSet().size();
        if (!wasFinished()) {
            YearMonthDay startDateToSearch = getLessonStartDay();
            YearMonthDay endDateToSearch = getLessonEndDay();
            count += getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch).size();
        }
        return count;
    }

    public SortedSet<YearMonthDay> getAllLessonDatesUntil(YearMonthDay day) {
        SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();
        if (day != null) {
            result.addAll(getAllLessonInstanceDatesUntil(day));
            if (!wasFinished()) {
                YearMonthDay startDateToSearch = getLessonStartDay();
                YearMonthDay lessonEndDay = getLessonEndDay();
                YearMonthDay endDateToSearch = (lessonEndDay.isAfter(day)) ? day : lessonEndDay;
                result.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
            }
        }
        return result;
    }

    public SortedSet<YearMonthDay> getAllLessonInstanceDates() {
        SortedSet<YearMonthDay> dates = new TreeSet<YearMonthDay>();
        for (LessonInstance instance : getLessonInstancesSet()) {
            dates.add(instance.getDay());
        }
        return dates;
    }

    public List<LessonInstance> getAllLessonInstancesUntil(LocalDate day) {
        List<LessonInstance> result = new ArrayList<LessonInstance>();
        if (day != null) {
            for (LessonInstance instance : getLessonInstancesSet()) {
                if (!instance.getDay().isAfter(day)) {
                    result.add(instance);
                }
            }
        }
        return result;
    }

    public SortedSet<YearMonthDay> getAllLessonInstanceDatesUntil(YearMonthDay day) {
        SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();
        if (day != null) {
            for (LessonInstance instance : getLessonInstancesSet()) {
                YearMonthDay instanceDay = instance.getDay();
                if (!instanceDay.isAfter(day)) {
                    result.add(instanceDay);
                }
            }
        }
        return result;
    }

    private SortedSet<YearMonthDay> getAllValidLessonDatesWithoutInstancesDates(YearMonthDay startDateToSearch,
            YearMonthDay endDateToSearch) {

        SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();
        startDateToSearch = startDateToSearch != null ? getValidBeginDate(startDateToSearch) : null;

        if (!wasFinished() && startDateToSearch != null && endDateToSearch != null
                && !startDateToSearch.isAfter(endDateToSearch)) {
            Space lessonCampus = getLessonCampus();
            final int dayIncrement = getFrequency() == FrequencyType.BIWEEKLY ? FrequencyType.WEEKLY
                    .getNumberOfDays() : getFrequency().getNumberOfDays();
            boolean shouldAdd = true;
            while (true) {
                if (isDayValid(startDateToSearch, lessonCampus)) {
                    if (getFrequency() != FrequencyType.BIWEEKLY || shouldAdd) {
                        if (!isHoliday(startDateToSearch, lessonCampus)) {
                            result.add(startDateToSearch);
                        }
                    }
                    shouldAdd = !shouldAdd;
                }
                startDateToSearch = startDateToSearch.plusDays(dayIncrement);
                if (startDateToSearch.isAfter(endDateToSearch)) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean isHoliday(YearMonthDay day, Space lessonCampus) {
        return Holiday.isHoliday(day.toLocalDate());
    }

    private boolean isDayValid(YearMonthDay day, Space lessonCampus) {
        return /* !Holiday.isHoliday(day.toLocalDate(), lessonCampus) && */getPeriod().nestedOccupationPeriodsContainsDay(day);
    }

    public YearMonthDay getNextPossibleLessonInstanceDate() {

        SortedSet<YearMonthDay> allLessonDates = getAllLessonDates();
        LessonInstance lastLessonInstance = getLastLessonInstance();

        if (lastLessonInstance != null) {
            YearMonthDay day = lastLessonInstance.getDay();
            SortedSet<YearMonthDay> nextLessonDates = allLessonDates.tailSet(day);
            nextLessonDates.remove(day);
            return nextLessonDates.isEmpty() ? null : nextLessonDates.first();
        }

        return allLessonDates.isEmpty() ? null : allLessonDates.first();
    }

    public LessonInstance getLastLessonInstance() {
        SortedSet<LessonInstance> result = new TreeSet<LessonInstance>(LessonInstance.COMPARATOR_BY_BEGIN_DATE_TIME);
        result.addAll(getLessonInstancesSet());
        return !result.isEmpty() ? result.last() : null;
    }

    public LessonInstance getFirstLessonInstance() {
        SortedSet<LessonInstance> result = new TreeSet<LessonInstance>(LessonInstance.COMPARATOR_BY_BEGIN_DATE_TIME);
        result.addAll(getLessonInstancesSet());
        return !result.isEmpty() ? result.first() : null;
    }

    private Summary getLastSummary() {
        SortedSet<Summary> summaries = getSummariesSortedByNewestDate();
        return (summaries.isEmpty()) ? null : summaries.first();
    }

    private SortedSet<Summary> getSummaries(Comparator<Summary> comparator) {
        SortedSet<Summary> lessonSummaries = new TreeSet<Summary>(comparator);
        lessonSummaries.addAll(getAssociatedSummaries());
        return lessonSummaries;
    }

    public LessonInstance getLessonInstanceFor(YearMonthDay date) {
        Collection<LessonInstance> lessonInstances = getLessonInstancesSet();
        for (LessonInstance lessonInstance : lessonInstances) {
            if (lessonInstance.getDay().isEqual(date)) {
                return lessonInstance;
            }
        }
        return null;
    }

    public boolean contains(Interval interval) {
        return contains(interval, getAllLessonDates());
    }

    public boolean isAllIntervalIn(Interval interval) {
        return isAllIntervalIn(interval, getAllLessonDates());
    }

    public boolean containsWithoutCheckInstanceDates(Interval interval) {
        return contains(interval, getAllLessonDatesWithoutInstanceDates());
    }

    private boolean isAllIntervalIn(Interval interval, SortedSet<YearMonthDay> allLessonDates) {

        YearMonthDay intervalStartDate = interval.getStart().toYearMonthDay();
        YearMonthDay intervalEndDate = interval.getEnd().toYearMonthDay();

        HourMinuteSecond intervalBegin =
                new HourMinuteSecond(interval.getStart().getHourOfDay(), interval.getStart().getMinuteOfHour(), 0);
        HourMinuteSecond intervalEnd =
                new HourMinuteSecond(interval.getEnd().getHourOfDay(), interval.getEnd().getMinuteOfHour(), 0);

        for (YearMonthDay day : allLessonDates) {
            if (intervalStartDate.isEqual(intervalEndDate)) {
                if (day.isEqual(intervalStartDate) && !intervalBegin.isBefore(getBeginHourMinuteSecond())
                        && !intervalEnd.isAfter(getEndHourMinuteSecond())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean contains(Interval interval, SortedSet<YearMonthDay> allLessonDates) {

        YearMonthDay intervalStartDate = interval.getStart().toYearMonthDay();
        YearMonthDay intervalEndDate = interval.getEnd().toYearMonthDay();

        HourMinuteSecond intervalBegin = new HourMinuteSecond(interval.getStart().getHourOfDay(),
                interval.getStart().getMinuteOfHour(), interval.getStart().getSecondOfMinute());
        HourMinuteSecond intervalEnd = new HourMinuteSecond(interval.getEnd().getHourOfDay(), interval.getEnd().getMinuteOfHour(),
                interval.getEnd().getSecondOfMinute());

        for (YearMonthDay day : allLessonDates) {
            if (intervalStartDate.isEqual(intervalEndDate)) {
                if (day.isEqual(intervalStartDate) && !intervalBegin.isAfter(getEndHourMinuteSecond())
                        && !intervalEnd.isBefore(getBeginHourMinuteSecond())) {
                    return true;
                }
            } else {
                if ((day.isAfter(intervalStartDate) && day.isBefore(intervalEndDate))
                        || day.isEqual(intervalStartDate) && !getEndHourMinuteSecond().isBefore(intervalBegin)
                        || (day.isEqual(intervalEndDate) && !getBeginHourMinuteSecond().isAfter(intervalEnd))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String prettyPrint() {
        final StringBuilder result = new StringBuilder();
        result.append(getDiaSemana().toString()).append(" (");
        result.append(getBeginHourMinuteSecond().toString("HH:mm")).append("-");
        result.append(getEndHourMinuteSecond().toString("HH:mm")).append(") ");
        result.append(hasSala() ? (getSala()).getName().toString() : "");
        return result.toString();
    }

    private void checkShiftLoad(Shift shift) {

        if (shift != null) {

            Collection<CourseLoad> courseLoads = shift.getCourseLoadsSet();

            if (courseLoads.size() == 1) {

                CourseLoad courseLoad = courseLoads.iterator().next();

                if (courseLoad.getUnitQuantity() != null && getUnitHours().compareTo(courseLoad.getUnitQuantity()) != 0) {
                    throw new DomainException("error.Lesson.shift.load.unit.quantity.exceeded", getUnitHours().toString(),
                            courseLoad.getUnitQuantity().toString());
                }

//                if (shift.getTotalHours().compareTo(courseLoad.getTotalQuantity()) == 1) {
//                    throw new DomainException("error.Lesson.shift.load.total.quantity.exceeded",
//                            shift.getTotalHours().toString(), courseLoad.getTotalQuantity().toString());
//                }

            } else if (courseLoads.size() > 1) {

                boolean unitValid = false, totalValid = false;
                BigDecimal lessonHours = getUnitHours();
                BigDecimal totalHours = shift.getTotalHours();

                for (CourseLoad courseLoad : courseLoads) {

                    unitValid = false;
                    totalValid = false;

                    if (courseLoad.getUnitQuantity() == null || lessonHours.compareTo(courseLoad.getUnitQuantity()) == 0) {
                        unitValid = true;
                    }
                    if (totalHours.compareTo(courseLoad.getTotalQuantity()) != 1) {
                        totalValid = true;
                        if (unitValid) {
                            break;
                        }
                    }
                }

//                if (!totalValid) {
//                    throw new DomainException("error.Lesson.shift.load.total.quantity.exceeded.2", shift.getTotalHours()
//                            .toString());
//                }
                if (!unitValid) {
                    throw new DomainException("error.Lesson.shift.load.unit.quantity.exceeded.2", getUnitHours().toString());
                }
            }
        }
    }

    public Calendar getInicio() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    public void setInicio(Calendar inicio) {
        if (inicio != null) {
            this.setBegin(inicio.getTime());
        } else {
            this.setBegin(null);
        }
    }

    public Calendar getFim() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setFim(Calendar fim) {
        if (fim != null) {
            this.setEnd(fim.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionPeriod().getAcademicInterval();
    }

    public List<EventBean> getAllLessonsEvents() {
        HashMap<DateTime, LessonInstance> hashmap = new HashMap<DateTime, LessonInstance>();
        ArrayList<EventBean> result = new ArrayList<EventBean>();
        LocalDate lessonEndDay = null;
        if (getLessonEndDay() != null) {
            getLessonEndDay().toLocalDate();
        }
        for (LessonInstance lessonInstance : getAllLessonInstancesUntil(lessonEndDay)) {
            hashmap.put(lessonInstance.getBeginDateTime(), lessonInstance);
        }

        for (YearMonthDay aDay : getAllLessonDates()) {
            DateTime beginDate = new DateTime(aDay.getYear(), aDay.getMonthOfYear(), aDay.getDayOfMonth(),
                    getBeginHourMinuteSecond().getHour(), getBeginHourMinuteSecond().getMinuteOfHour(),
                    getBeginHourMinuteSecond().getSecondOfMinute(), 0);

            LessonInstance lessonInstance = hashmap.get(beginDate);
            EventBean bean;
            Set<Space> location = new HashSet<>();

            String url = getExecutionCourse().getSiteUrl();

            if (lessonInstance != null) {

                if (lessonInstance.getLessonInstanceSpaceOccupation() != null) {
                    location.addAll(lessonInstance.getLessonInstanceSpaceOccupation().getSpaces());
                }
                String summary = null;
                if (lessonInstance.getSummary() != null) {
                    summary = lessonInstance.getSummary().getSummaryText().getContent();
                    Pattern p = Pattern.compile("<[a-zA-Z0-9\\/]*[^>]*>");
                    Matcher matcher = p.matcher(summary);
                    summary = matcher.replaceAll("");

                    p = Pattern.compile("\\s(\\s)*");
                    matcher = p.matcher(summary);
                    summary = matcher.replaceAll(" ");
                }

                bean = new ClassEventBean(lessonInstance.getBeginDateTime(), lessonInstance.getEndDateTime(), false, location,
                        url + "/sumarios", summary, getShift());
            } else {
                if (getLessonSpaceOccupation() != null) {
                    location.add(getLessonSpaceOccupation().getSpace());
                }
                DateTime endDate = new DateTime(aDay.getYear(), aDay.getMonthOfYear(), aDay.getDayOfMonth(),
                        getEndHourMinuteSecond().getHour(), getEndHourMinuteSecond().getMinuteOfHour(),
                        getEndHourMinuteSecond().getSecondOfMinute(), 0);

                bean = new ClassEventBean(beginDate, endDate, false, location, url, null, getShift());
            }

            result.add(bean);
        }

        return result;
    }

    @Deprecated
    public java.util.Date getBegin() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getBeginHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setBegin(java.util.Date date) {
        if (date == null) {
            setBeginHourMinuteSecond(null);
        } else {
            setBeginHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnd() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEndHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnd(java.util.Date date) {
        if (date == null) {
            setEndHourMinuteSecond(null);
        } else {
            setEndHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public void setDiaSemana(DiaSemana diaSemana) {
        setWeekDay(diaSemana == null ? null : WeekDay.getWeekDay(diaSemana));
    }

    @Deprecated
    public DiaSemana getDiaSemana() {
        return DiaSemana.fromWeekDay(getWeekDay());
    }

    private static DateTime toDateTime(final YearMonthDay ymd, final HourMinuteSecond hms) {
        return new DateTime(ymd.getYear(), ymd.getMonthOfYear(), ymd.getDayOfMonth(), hms.getHour(), hms.getMinuteOfHour(),
                hms.getSecondOfMinute(), 0);
    }

    public Set<Interval> getAllLessonIntervals() {
        Set<Interval> intervals = new HashSet<Interval>();
        for (LessonInstance instance : getLessonInstancesSet()) {
            intervals.add(new Interval(instance.getBeginDateTime(), instance.getEndDateTime()));
        }
        if (!wasFinished()) {
            YearMonthDay startDateToSearch = getLessonStartDay();
            YearMonthDay endDateToSearch = getLessonEndDay();
            for (YearMonthDay day : getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch)) {
                intervals.add(
                        new Interval(toDateTime(day, getBeginHourMinuteSecond()), toDateTime(day, getEndHourMinuteSecond())));
            }
        }
        return intervals;
    }

    public String getOccurrenceWeeksAsString() {
        final SortedSet<Integer> weeks = new TreeSet<Integer>();

        final ExecutionCourse executionCourse = getExecutionCourse();
        final Interval maxLessonsInterval = executionCourse.getMaxLessonsInterval();
        final LocalDate start = maxLessonsInterval.getStart().toLocalDate();
        for (final Interval interval : getAllLessonIntervals()) {
            final Integer week = Weeks.weeksBetween(start, interval.getStart().toLocalDate()).getWeeks() + 1;
            weeks.add(week);
        }

        final StringBuilder builder = new StringBuilder();
        final Integer[] weeksA = weeks.toArray(new Integer[0]);
        for (int i = 0; i < weeksA.length; i++) {
            if (i == 0) {
                builder.append(weeksA[i]);
            } else if (i == weeksA.length - 1 || (weeksA[i]) + 1 != (weeksA[i + 1])) {
                final String seperator = (weeksA[i - 1]) + 1 == (weeksA[i]) ? " - " : ", ";
                builder.append(seperator);
                builder.append(weeksA[i]);
            } else if ((weeksA[i - 1]) + 1 != weeksA[i]) {
                builder.append(", ");
                builder.append(weeksA[i]);
            }
        }
        return builder.toString();
    }

    private boolean hasAnyLessonInstances() {
        return !getLessonInstancesSet().isEmpty();
    }

    public boolean isBiWeeklyOffset() {
        return getFrequency() == FrequencyType.BIWEEKLY;
    }

}
