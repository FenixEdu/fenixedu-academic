/*
 * Created on Jun 2, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * @author velouria@velouria.org & Prof. J. Cachopo
 *
 */

public class WorkWeek implements Serializable {
    
    public static final WorkWeek WORK_WEEK = new WorkWeek(EnumSet.range(WeekDays.MONDAY, WeekDays.FRIDAY));

    // set containing the week days
    private EnumSet<WeekDays> days;

    public WorkWeek(EnumSet<WeekDays> days) {
      this.days = days;
    }

    public WorkWeek(WeekDays... days) {
        EnumSet<WeekDays> myDays = EnumSet.noneOf(WeekDays.class);
        for (WeekDays day : days) {
          myDays.add(day);
        }
        this.days = myDays;
      }

    public EnumSet<WeekDays> getDays() {
        return days;
    }
    
    public boolean worksAt(WeekDays day) {
        return this.getDays().contains(day);
    }

    // counts how many days per week the employee works
    public int workDaysPerWeek() {
        int workDays = 0;
        for (WeekDays day: this.getDays()) {
            if (this.getDays().contains(day)) {
                workDays++;
            }
        }
        return workDays;
    }
    
    public boolean equals(WorkWeek workWeek) {
        return workWeek.getDays().containsAll(this.getDays());
    }

    // This work week contains workWeek 
    public boolean contains(WorkWeek otherWorkWeek) {
        for (WeekDays otherWorkWeekDay : otherWorkWeek.getDays()) {
            if (this.getDays().contains(otherWorkWeekDay))
                return true;
        }
        return false;
    }
    
    // TRASH
    public void printf() {
        for (WeekDays day: this.getDays()) {
            System.out.println(day.toString());
        }
    }

}
