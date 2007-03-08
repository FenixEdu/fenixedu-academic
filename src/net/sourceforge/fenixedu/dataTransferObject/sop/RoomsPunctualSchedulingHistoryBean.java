package net.sourceforge.fenixedu.dataTransferObject.sop;

import java.io.Serializable;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class RoomsPunctualSchedulingHistoryBean implements Serializable {

    private Partial year;
    
    private Partial month;
    
    public RoomsPunctualSchedulingHistoryBean() {
	setYear(new Partial(DateTimeFieldType.year(), new YearMonthDay().getYear()));
	setMonth(new Partial(DateTimeFieldType.monthOfYear(), new YearMonthDay().getMonthOfYear()));
    }

    public Partial getMonth() {
        return month;
    }

    public void setMonth(Partial month) {
        this.month = month;
    }

    public Partial getYear() {
        return year;
    }

    public void setYear(Partial year) {
        this.year = year;
    }
}
