package net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;

import org.jfree.data.time.Year;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class CalendarEntryBean implements Serializable {

    private DomainReference<AcademicCalendarEntry> parentEntryReference;
    
    private DomainReference<AcademicCalendar> academicCalendarReference;
    
    private Class type;
    
    private Partial beginDateToDisplay;
    
    private Partial endDateToDisplay;
           
    
    public CalendarEntryBean(AcademicCalendarEntry academicCalendarEntry, Partial begin, Partial end) {
	setParentEntry(academicCalendarEntry);
	setBeginDateToDisplay(begin);
	setEndDateToDisplay(end);
    }
        
    public CalendarEntryBean(AcademicCalendar academicCalendar, Partial begin, Partial end) {
	setAcademicCalendar(academicCalendar);
	setBeginDateToDisplay(begin);
	setEndDateToDisplay(end);
    }
    
    public AcademicCalendar getAcademicCalendar() {
	return academicCalendarReference == null ? null : academicCalendarReference.getObject();
    }
    
    public void setAcademicCalendar(AcademicCalendar academicCalendar) {
	this.academicCalendarReference = academicCalendar == null ? null : new DomainReference<AcademicCalendar>(academicCalendar);
    }
    
    public AcademicCalendarEntry getParentEntry() {
	return parentEntryReference == null ? null : parentEntryReference.getObject();
    }
    
    public void setParentEntry(AcademicCalendarEntry academicCalendarEntry) {
	this.parentEntryReference = academicCalendarEntry == null ? null : new DomainReference<AcademicCalendarEntry>(academicCalendarEntry);
    }  

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Partial getBeginDateToDisplay() {
        return beginDateToDisplay;
    }

    public void setBeginDateToDisplay(Partial beginDateToDisplay) {
        this.beginDateToDisplay = beginDateToDisplay;
    }

    public Partial getEndDateToDisplay() {
        return endDateToDisplay;
    }

    public void setEndDateToDisplay(Partial endDateToDisplay) {
        this.endDateToDisplay = endDateToDisplay;
    }
    
    public String getBeginPartialString() {
	return getPartialString(getBeginDateToDisplay());
    }
    
    public String getEndPartialString() {
	return getPartialString(getEndDateToDisplay());
    }
    
    public static String getPartialString(Partial partial) {
	return partial.toString("MMyyyy");
    }
    
    public static Partial getPartialFromString(String date) {
	Integer month = Integer.valueOf(date.substring(0, 2));
	Integer year = Integer.valueOf(date.substring(2));
	return new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {year.intValue(), month.intValue()});
    }
    
    public static Partial getPartialFromYearMonthDay(YearMonthDay day) {
	return new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {day.getYear(), day.getMonthOfYear()});	
    }
    
    public static YearMonthDay getDateFromPartial(Partial partial) {
	return new YearMonthDay(partial.get(DateTimeFieldType.year()), partial.get(DateTimeFieldType.monthOfYear()), 1);	
    }

    public YearMonthDay getBeginDateToDisplayInYearMonthDayFormat() {
	return getDateFromPartial(getBeginDateToDisplay()); 	
    }
    
    public YearMonthDay getEndDateToDisplayInYearMonthDayFormat() {
	return getDateFromPartial(getEndDateToDisplay()); 	
    }
}
