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
package net.sourceforge.fenixedu.util.renderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GanttDiagram {

    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_BEGIN = new Comparator<Interval>() {

        @Override
        public int compare(Interval o1, Interval o2) {
            return o1.getStart().compareTo(o2.getStart());
        }

    };
    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_END = new Comparator<Interval>() {

        @Override
        public int compare(Interval o1, Interval o2) {
            return o1.getEnd().compareTo(o2.getEnd());
        }

    };

    private Locale locale;

    private List<? extends GanttDiagramEvent> events;

    private ViewType viewType;

    private DateTime firstInstant, lastInstant;

    private Map<Integer, Integer> yearsView;

    private Map<YearMonthDay, Integer> monthsView;

    private List<DateTime> months, days;

    public static GanttDiagram getNewTotalGanttDiagram(List<? extends GanttDiagramEvent> events_) {
        return new GanttDiagram(events_, ViewType.TOTAL);
    }

    public static GanttDiagram getNewTotalGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin,
            YearMonthDay end) {
        return new GanttDiagram(events_, ViewType.TOTAL, begin, end);
    }

    public static GanttDiagram getNewWeeklyGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin) {
        return new GanttDiagram(events_, ViewType.WEEKLY, begin);
    }

    public static GanttDiagram getNewDailyGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin) {
        return new GanttDiagram(events_, ViewType.DAILY, begin);
    }

    public static GanttDiagram getNewMonthlyGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin) {
        return new GanttDiagram(events_, ViewType.MONTHLY, begin);
    }

    public static GanttDiagram getNewMonthlyTotalGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin) {
        return new GanttDiagram(events_, ViewType.MONTHLY_TOTAL, begin);
    }

    public static GanttDiagram getNewYearDailyGanttDiagram(List<? extends GanttDiagramEvent> events_, YearMonthDay begin) {
        return new GanttDiagram(events_, ViewType.YEAR_DAILY, begin);
    }

    private GanttDiagram(List<? extends GanttDiagramEvent> events_, ViewType type) {
        setViewType(type);
        setEvents(events_);
        init(type, null, null);
    }

    private GanttDiagram(List<? extends GanttDiagramEvent> events_, ViewType type, YearMonthDay begin) {
        setViewType(type);
        setEvents(events_);
        init(type, begin, null);
    }

    private GanttDiagram(List<? extends GanttDiagramEvent> events_, ViewType type, YearMonthDay begin, YearMonthDay end) {
        setViewType(type);
        setEvents(events_);
        init(type, begin, end);
    }

    private void init(ViewType type, YearMonthDay begin, YearMonthDay end) {
        switch (type) {

        case TOTAL:
            calculateFirstAndLastInstantInTotalMode(begin, end);
            generateYearsViewAndMonths();
            break;

        case MONTHLY_TOTAL:
            calculateFirstAndLastInstantInMonthlyMode(begin);
            generateYearsViewAndMonths();
            break;

        case MONTHLY:
            calculateFirstAndLastInstantInMonthlyMode(begin);
            generateYearsViewMonthsViewAndDays();
            break;

        case WEEKLY:
            calculateFirstAndLastInstantInWeeklyMode(begin);
            generateYearsViewMonthsViewAndDays();
            break;

        case DAILY:
            calculateFirstAndLastInstantInDailyMode(begin);
            generateYearsViewMonthsViewAndDays();
            break;

        case YEAR_DAILY:
            calculateFirstAndLastInstantInMonthlyMode(begin);
            generateYearsViewMonthsViewAndDays();
            break;

        default:
            break;
        }
    }

    private void calculateFirstAndLastInstantInMonthlyMode(YearMonthDay begin) {
        if (begin == null) {
            throw new IllegalArgumentException();
        }
        DateTime beginDateTime = begin.toDateTimeAtMidnight();
        beginDateTime = (beginDateTime.getDayOfMonth() != 1) ? beginDateTime.withDayOfMonth(1) : beginDateTime;
        setFirstInstant(beginDateTime);
        setLastInstant(beginDateTime.plusMonths(1).minusDays(1));
    }

    private void calculateFirstAndLastInstantInDailyMode(YearMonthDay begin) {
        if (begin == null) {
            throw new IllegalArgumentException();
        }
        setFirstInstant(begin.toDateTimeAtMidnight());
        setLastInstant(begin.toDateTimeAtMidnight());
    }

    private void calculateFirstAndLastInstantInWeeklyMode(YearMonthDay begin) {
        if (begin == null) {
            throw new IllegalArgumentException();
        }
        DateTime beginDateTime = begin.toDateTimeAtMidnight();
        beginDateTime = (beginDateTime.getDayOfWeek() != 1) ? beginDateTime.withDayOfWeek(1) : beginDateTime;
        setFirstInstant(beginDateTime);
        setLastInstant(beginDateTime.plusDays(6));
    }

    private void calculateFirstAndLastInstantInTotalMode(YearMonthDay begin, YearMonthDay end) {

        if ((begin != null && end == null) || (end != null && begin == null)) {
            throw new IllegalArgumentException();
        }

        if (begin == null) {

            SortedSet<Interval> allIntervalsSortedByBeginDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_BEGIN);
            for (GanttDiagramEvent event : getEvents()) {
                allIntervalsSortedByBeginDate.addAll(event.getGanttDiagramEventSortedIntervals());
            }
            if (!allIntervalsSortedByBeginDate.isEmpty()) {
                setFirstInstant(allIntervalsSortedByBeginDate.first().getStart().toDateMidnight().toDateTime());
            }

            SortedSet<Interval> allIntervalsSortedByEndDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_END);
            for (GanttDiagramEvent event : getEvents()) {
                allIntervalsSortedByEndDate.addAll(event.getGanttDiagramEventSortedIntervals());
            }
            if (!allIntervalsSortedByEndDate.isEmpty()) {
                setLastInstant(allIntervalsSortedByEndDate.last().getEnd().toDateMidnight().toDateTime());
            }

        } else {
            setFirstInstant(begin.toDateTimeAtMidnight());
            setLastInstant(end.toDateTimeAtMidnight());
        }

        if ((getFirstInstant() != null && getLastInstant() != null)
                && (getFirstInstant().isAfter(getLastInstant()) || getLastInstant().isBefore(getFirstInstant()))) {
            throw new IllegalArgumentException();
        }
    }

    private void generateYearsViewMonthsViewAndDays() {

        DateTime firstMonthDateTime = getFirstInstant();
        DateTime lastMontDateTime = getLastInstant();

        if (firstMonthDateTime != null && lastMontDateTime != null) {
            while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
                    || (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && firstMonthDateTime.getDayOfYear() <= lastMontDateTime
                            .getDayOfYear())) {

                getDays().add(firstMonthDateTime);

                YearMonthDay day = firstMonthDateTime.toYearMonthDay().withDayOfMonth(1);
                if (getMonthsView().containsKey(day)) {
                    getMonthsView().put(day, getMonthsView().get(day) + 1);
                } else {
                    getMonthsView().put(day, 1);
                }

                if (getYearsView().containsKey(Integer.valueOf(firstMonthDateTime.getYear()))) {
                    getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()),
                            getYearsView().get(Integer.valueOf(firstMonthDateTime.getYear())) + 1);
                } else {
                    getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()), 1);
                }

                firstMonthDateTime = firstMonthDateTime.plusDays(1);
            }
        }
    }

    private void generateYearsViewAndMonths() {

        DateTime firstMonthDateTime = getFirstInstant();
        DateTime lastMontDateTime = getLastInstant();

        if (firstMonthDateTime != null && lastMontDateTime != null) {
            while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
                    || (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && firstMonthDateTime.getMonthOfYear() <= lastMontDateTime
                            .getMonthOfYear())) {

                getMonths().add(firstMonthDateTime);

                if (getYearsView().containsKey(Integer.valueOf(firstMonthDateTime.getYear()))) {
                    getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()),
                            getYearsView().get(Integer.valueOf(firstMonthDateTime.getYear())) + 1);
                } else {
                    getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()), 1);
                }

                firstMonthDateTime = firstMonthDateTime.plusMonths(1);
            }
        }
    }

    public List<? extends GanttDiagramEvent> getEvents() {
        return events;
    }

    public void setEvents(List<? extends GanttDiagramEvent> events) {
        this.events = events;
    }

    public DateTime getFirstInstant() {
        return firstInstant;
    }

    public void setFirstInstant(DateTime firstInstant) {
        this.firstInstant = firstInstant;
    }

    public DateTime getLastInstant() {
        return lastInstant;
    }

    public void setLastInstant(DateTime lastInstant) {
        this.lastInstant = lastInstant;
    }

    public List<DateTime> getMonths() {
        if (months == null) {
            months = new ArrayList<DateTime>();
        }
        return months;
    }

    public void setMonths(List<DateTime> months) {
        this.months = months;
    }

    public int getMonthsDaysSize() {
        int result = 0;
        for (DateTime month : getMonths()) {
            DateTime firstDayOfMonth = (month.getDayOfMonth() != 1) ? month.withDayOfMonth(1) : month;
            DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
            int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
            result += monthNumberOfDays;
        }
        return result;
    }

    public Map<Integer, Integer> getYearsView() {
        if (yearsView == null) {
            yearsView = new TreeMap<Integer, Integer>();
        }
        return yearsView;
    }

    public void setYearsView(Map<Integer, Integer> years) {
        this.yearsView = years;
    }

    public Map<YearMonthDay, Integer> getMonthsView() {
        if (monthsView == null) {
            monthsView = new TreeMap<YearMonthDay, Integer>();
        }
        return monthsView;
    }

    public void setMonthsView(Map<YearMonthDay, Integer> monthsView) {
        this.monthsView = monthsView;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public Locale getLocale() {
        if (locale == null) {
            locale = I18N.getLocale();
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<DateTime> getDays() {
        if (days == null) {
            days = new ArrayList<DateTime>();
        }
        return days;
    }

    public void setDays(List<DateTime> days) {
        this.days = days;
    }

    public enum ViewType {
        TOTAL, MONTHLY, MONTHLY_TOTAL, WEEKLY, DAILY, YEAR_DAILY;
    }
}
