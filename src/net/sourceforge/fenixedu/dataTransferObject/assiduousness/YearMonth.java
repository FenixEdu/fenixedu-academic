package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class YearMonth implements Serializable {
    Integer year;

    Month month;

    public YearMonth() {
        super();
    }

    public YearMonth(YearMonthDay date) {
        super();
        setYear(date.getYear());
        setMonth(Month.values()[date.getMonthOfYear() - 1]);
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean getIsThisYearMonthClosed() {
        Partial yearMonth = new Partial()
                .with(DateTimeFieldType.monthOfYear(), getMonth().ordinal() + 1).with(
                        DateTimeFieldType.year(), getYear());
        return ClosedMonth.isMonthClosed(yearMonth);
    }
}
