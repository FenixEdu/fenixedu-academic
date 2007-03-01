package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

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

    public YearMonth(int year, int month) {
	super();
	setYear(year);
	setMonth(Month.values()[month - 1]);
    }

    public YearMonth(YearMonthDay date) {
	super();
	setYear(date.getYear());
	setMonth(Month.values()[date.getMonthOfYear() - 1]);
    }

    public YearMonth(Integer year, Month month) {
	setYear(year);
	setMonth(month);
    }

    public Month getMonth() {
	return month;
    }

    public int getNumberOfMonth() {
	return getMonth().getNumberOfMonth();
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

    public boolean getIsThisYearMonthClosedForExtraWork() {
	Partial yearMonth = new Partial()
		.with(DateTimeFieldType.monthOfYear(), getMonth().ordinal() + 1).with(
			DateTimeFieldType.year(), getYear());
	return ClosedMonth.isMonthClosedForExtraWork(yearMonth);
    }

    public boolean getCanCloseMonth() {
	Partial yearMonth = new Partial()
		.with(DateTimeFieldType.monthOfYear(), getMonth().ordinal() + 1).with(
			DateTimeFieldType.year(), getYear());
	return ClosedMonth.getCanCloseMonth(yearMonth);
    }

    public void addMonth() {
	if (getNumberOfMonth() == 12) {
	    setMonth(Month.values()[0]);
	    setYear(getYear() + 1);
	} else {
	    setMonth(Month.values()[getNumberOfMonth()]);
	}
    }

}
