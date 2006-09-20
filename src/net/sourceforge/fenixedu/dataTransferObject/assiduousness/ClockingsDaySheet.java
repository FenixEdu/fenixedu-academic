package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.YearMonthDay;

public class ClockingsDaySheet implements Serializable {
    YearMonthDay date;

    List<Clocking> clockings;

    public List<Clocking> getClockings() {
        if (clockings == null) {
            clockings = new ArrayList<Clocking>();
        }
        return clockings;
    }

    public void setClockings(List<Clocking> clockings) {
        this.clockings = clockings;
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public void addClocking(Clocking clocking) {
        getClockings().add(clocking);
    }

    public String getWeekDay() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
        return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtMidnight())
                .toString() + "_ACRONYM");
    }
}
