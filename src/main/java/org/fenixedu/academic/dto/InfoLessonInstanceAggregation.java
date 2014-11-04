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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.joda.time.YearMonthDay;

public class InfoLessonInstanceAggregation extends InfoShowOccupation {

    private static final long serialVersionUID = 1L;

    private final Shift shift;
    private final int weekDay;
    private final HourMinuteSecond begin;
    private final HourMinuteSecond end;
    private final Space allocatableSpace;
    private final SortedSet<LocalDate> dates = new TreeSet<LocalDate>();

    public InfoLessonInstanceAggregation(final Shift shift, final int weekDay, final HourMinuteSecond begin,
            final HourMinuteSecond end, final Space allocatableSpace) {
        this.shift = shift;
        this.weekDay = weekDay;
        this.begin = begin;
        this.end = end;
        this.allocatableSpace = allocatableSpace;
    }

    public InfoLessonInstanceAggregation(final Shift shift, final LessonInstance instance) {
        this.shift = shift;
        this.weekDay = instance.getBeginDateTime().getDayOfWeek();
        this.begin = instance.getStartTime();
        this.end = instance.getEndTime();
        this.allocatableSpace = instance.getRoom();
    }

    public InfoLessonInstanceAggregation(final Shift shift, final Lesson lesson, final YearMonthDay yearMonthDay) {
        this.shift = shift;
        this.weekDay = yearMonthDay.toDateMidnight().getDayOfWeek();
        this.begin = lesson.getBeginHourMinuteSecond();
        this.end = lesson.getEndHourMinuteSecond();
        this.allocatableSpace = lesson.getSala();
    }

    private void register(final YearMonthDay yearMonthDay) {
        dates.add(new LocalDate(yearMonthDay.getYear(), yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth()));
    }

    @Override
    public InfoShift getInfoShift() {
        return new InfoShift(shift);
    }

    @Override
    public ShiftType getTipo() {
        return null;
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
        throw new Error("Not implemented.");
    }

    @Override
    public DiaSemana getDiaSemana() {
        return DiaSemana.fromJodaWeekDay(weekDay);
    }

    @Override
    public Calendar getInicio() {
        throw new Error("Not implemented.");
    }

    @Override
    public Calendar getFim() {
        throw new Error("Not implemented.");
    }

    @Override
    public int getLastHourOfDay() {
        final int hourOfDay = end.getHour();
        return end.getMinuteOfHour() > 0 ? hourOfDay + 1 : hourOfDay;
    }

    @Override
    public int getFirstHourOfDay() {
        return begin.getHour();
    }

    @Override
    public HourMinuteSecond getBeginHourMinuteSecond() {
        return begin;
    }

    @Override
    public HourMinuteSecond getEndHourMinuteSecond() {
        return end;
    }

    @Override
    public Space getAllocatableSpace() {
        return allocatableSpace;
    }

    public Shift getShift() {
        return shift;
    }

    public SortedSet<LocalDate> getDates() {
        return dates;
    }

    public SortedSet<Integer> getWeeks() {
        final ExecutionCourse executionCourse = shift.getExecutionCourse();
        final YearMonthDay firstPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getLeft();
        final YearMonthDay lastPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getRight();
        return getWeeks(new Interval(firstPossibleLessonDay.toDateTimeAtMidnight(), lastPossibleLessonDay.toDateTimeAtMidnight()
                .plusDays(1)));
    }

    public SortedSet<Integer> getWeeks(final Interval lessonInterval) {
        final SortedSet<Integer> weeks = new TreeSet<Integer>();
        final LocalDate firstPossibleLessonDay = lessonInterval.getStart().toLocalDate();
        for (final LocalDate localDate : dates) {
            final Integer week = Weeks.weeksBetween(firstPossibleLessonDay, localDate).getWeeks() + 1;
            weeks.add(week);
        }
        return weeks;
    }

    public boolean availableInAllWeeks() {
        // TODO: if this is properly implemented it should look for gaps in the schedule.
        return false;
    }

    public static Collection<InfoLessonInstanceAggregation> getAggregations(final Shift shift) {
        final Map<String, InfoLessonInstanceAggregation> result = new TreeMap<String, InfoLessonInstanceAggregation>();
        for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
            for (final LessonInstance instance : lesson.getLessonInstancesSet()) {
                final String key = key(instance);
                if (!result.containsKey(key)) {
                    result.put(key, new InfoLessonInstanceAggregation(shift, instance));
                }
                final InfoLessonInstanceAggregation aggregation = result.get(key);
                aggregation.register(instance.getDay());
            }
            for (final YearMonthDay yearMonthDay : lesson.getAllLessonDatesWithoutInstanceDates()) {
                final String key = key(lesson, yearMonthDay);
                if (!result.containsKey(key)) {
                    result.put(key, new InfoLessonInstanceAggregation(shift, lesson, yearMonthDay));
                }
                final InfoLessonInstanceAggregation aggregation = result.get(key);
                aggregation.register(yearMonthDay);
            }
        }
        return result.values();
    }

    private static String key(final Lesson lesson, final YearMonthDay yearMonthDay) {
        final StringBuilder key = new StringBuilder();
        key.append(yearMonthDay.toDateMidnight().getDayOfWeek());
        key.append('_');
        key.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
        key.append('_');
        key.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
        key.append('_');
        key.append(lesson.getSala() == null ? "" : lesson.getSala().getExternalId());
        return key.toString();
    }

    private static String key(final LessonInstance instance) {
        final StringBuilder key = new StringBuilder();
        key.append(instance.getBeginDateTime().getDayOfWeek());
        key.append('_');
        key.append(instance.getStartTime().toString("HH:mm"));
        key.append('_');
        key.append(instance.getEndTime().toString("HH:mm"));
        key.append('_');
        key.append(instance.getRoom() == null ? "" : instance.getRoom().getExternalId());
        return key.toString();
    }

    @Override
    public String getExternalId() {
        final StringBuilder sb = new StringBuilder();
        sb.append(shift.getExternalId()).append(weekDay).append(begin.toString("HH:mm:ss")).append(end.toString("HH:mm:ss"));
        if (allocatableSpace != null) {
            sb.append(allocatableSpace.getExternalId());
        }
        return sb.toString();
    }

    public String prettyPrintWeeks() {
        return prettyPrintWeeks(getWeeks());
    }

    public static String prettyPrintWeeks(SortedSet<Integer> weeks) {
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
}
