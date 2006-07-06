package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkDaySheet implements Serializable {
    YearMonthDay date;

    String workScheduleAcronym;

    Period balanceTime;

    Duration unjustifiedTime;

    String notes;

    List<TimeOfDay> clockings = new ArrayList<TimeOfDay>();

    public Period getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(Period balanceTime) {
        this.balanceTime = balanceTime;
    }

    public List<TimeOfDay> getClockings() {
        return clockings;
    }

    public void setClockings(List<TimeOfDay> clockings) {
        this.clockings = clockings;
    }

    public void addClockings(TimeOfDay clocking) {
        this.clockings.add(clocking);
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Duration getUnjustifiedTime() {
        return unjustifiedTime;
    }

    public void setUnjustifiedTime(Duration unjustifiedTime) {
        this.unjustifiedTime = unjustifiedTime;
    }

    public String getWorkScheduleAcronym() {
        return workScheduleAcronym;
    }

    public void setWorkScheduleAcronym(String workScheduleAcronym) {
        this.workScheduleAcronym = workScheduleAcronym;
    }

    public String getDateFormatted() {
        if (getDate() == null) {
            return "";
        }
        return getDate().toString("dd/MM/yyyy");
    }

    public String getBalanceTimeFormatted() {
        Period balancePeriod = getBalanceTime();
        StringBuffer result = new StringBuffer();
        result.append(balancePeriod.getHours());
        result.append(":");
        if (balancePeriod.getMinutes() > -10 && balancePeriod.getMinutes() < 10) {
            result.append("0");
        }
        if (balancePeriod.getMinutes() < 0) {
            result.append((-balancePeriod.getMinutes()));
            if (!result.toString().startsWith("-")) {
                result = new StringBuffer("-").append(result);
            }
        } else {
            result.append(balancePeriod.getMinutes());
        }
        return result.toString();
    }

    public String getUnjustifiedTimeFormatted() {
        Period unjustifiedPeriod = getUnjustifiedTime().toPeriod();
        StringBuffer result = new StringBuffer();
        result.append(unjustifiedPeriod.getHours());
        result.append(":");
        if (unjustifiedPeriod.getMinutes() > -10 && unjustifiedPeriod.getMinutes() < 10) {
            result.append("0");
        }
        if (unjustifiedPeriod.getMinutes() < 0) {
            result.append((-unjustifiedPeriod.getMinutes()));
            if (!result.toString().startsWith("-")) {
                result = new StringBuffer("-").append(result);
            }
        } else {
            result.append(unjustifiedPeriod.getMinutes());
        }
        return result.toString();
    }

    public String getClockingsFormatted() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        StringBuffer result = new StringBuffer();
        for (TimeOfDay timeOfDay : getClockings()) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(fmt.print(timeOfDay));
        }
        return result.toString();
    }

    public String getWeekDay() {
        if (getDate() == null) {
            return "";
        }
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources");
        return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtMidnight())
                .toString()
                + "_ACRONYM");
    }
}
