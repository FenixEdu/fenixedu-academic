/*
 * Created on Jun 2, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.io.Serializable;
import java.util.EnumSet;

import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateTime;

/**
 * @author velouria@velouria.org & Prof. J. Cachopo
 * 
 */

public class WorkWeek implements Serializable {

    public static final WorkWeek WORK_WEEK = new WorkWeek(EnumSet.range(WeekDay.MONDAY, WeekDay.SUNDAY));

    // set containing the week days
    private EnumSet<WeekDay> days;

    public WorkWeek(EnumSet<WeekDay> newDays) {
        days = newDays;
    }

    public WorkWeek(WeekDay... newDays) {
        EnumSet<WeekDay> myDays = EnumSet.noneOf(WeekDay.class);
        for (WeekDay day : newDays) {
            myDays.add(day);
        }
        days = myDays;
    }

    public EnumSet<WeekDay> getDays() {
        return days;
    }

    public boolean worksAt(WeekDay day) {
        return getDays().contains(day);
    }

    // counts how many days per week the employee works
    public int workDaysPerWeek() {
        int workDays = 0;
        for (WeekDay day : getDays()) {
            if (getDays().contains(day)) {
                workDays++;
            }
        }
        return workDays;
    }

    public boolean equals(WorkWeek workWeek) {
        return workWeek.getDays().containsAll(getDays()) && getDays().containsAll(workWeek.getDays());
    }

    // This work week contains workWeek
    public boolean contains(WorkWeek otherWorkWeek) {
        for (WeekDay otherWorkWeekDay : otherWorkWeek.getDays()) {
            if (getDays().contains(otherWorkWeekDay))
                return true;
        }
        return false;
    }

    public boolean contains(DateTime date) {
        WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(date);
        if (getDays().contains(dayOfWeek)) {
            return true;
        }
        return false;
    }
}
