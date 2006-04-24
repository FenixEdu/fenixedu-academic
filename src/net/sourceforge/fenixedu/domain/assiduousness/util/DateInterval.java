package net.sourceforge.fenixedu.domain.assiduousness.util;

import org.joda.time.YearMonthDay;
import org.joda.time.DateTime;

// The purpose of this class is to define Intervals with partials
public class DateInterval {

    // TODO mudar para immutable
    private YearMonthDay startDate;
    private YearMonthDay endDate;

    public DateInterval(YearMonthDay startDate, YearMonthDay endDate) {
    		setStartDate(startDate);
    		setEndDate(endDate);
    }
    
    public void setStartDate(YearMonthDay newStartDate) {
    		startDate = newStartDate;
    }
    
    public void setEndDate(YearMonthDay newEndDate) {
    		endDate = newEndDate;
    }
    
    public YearMonthDay getStartDate() {
    		return startDate;
    }

    public YearMonthDay getEndDate() {
		return endDate;
    }

    // Returns true if the DateInterval contains a generic DateTime;
    public boolean containsDate(DateTime date) {
    		YearMonthDay yearMonthDayDate = date.toYearMonthDay();
    		return containsDate(yearMonthDayDate);
    }
    
    public boolean containsDate(YearMonthDay date) {
    		return ((getStartDate().isEqual(date) || getStartDate().isBefore(date)) &&
    				(getEndDate().isEqual(date) || getEndDate().isAfter(date)));
    }

    // Returns true if this DateInterval contains interval
    public boolean containsInterval(DateInterval interval) {
    		return (containsDate(interval.getStartDate()) || containsDate(interval.getEndDate()));
    }
    
    public String toString() {
    		return new String(getStartDate().toString() + " - " + getEndDate().toString());
    }
    
}
