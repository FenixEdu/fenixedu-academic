package net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class CalendarEntryBean implements Serializable {
       
    private Class type;
    
    private Partial beginDateToDisplay;
    
    private Partial endDateToDisplay;

    private DomainReference<AcademicCalendarEntry> entryReference;
    
    private DomainReference<AcademicCalendarEntry> templateEntryReference;
    
    private DomainReference<AcademicCalendarRootEntry> selectedRootEntryReference;
    
    public CalendarEntryBean(AcademicCalendarRootEntry rootEntry, AcademicCalendarEntry academicCalendarEntry, Partial begin, Partial end) {	
	setRootEntry(rootEntry);
	setEntry(academicCalendarEntry);
	setBeginDateToDisplay(begin);
	setEndDateToDisplay(end);
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {
	return getEntry().getRooEntry();
    }
    
    public AcademicCalendarRootEntry getRootEntry() {
	return selectedRootEntryReference == null ? null : selectedRootEntryReference.getObject();
    }
    
    public void setRootEntry(AcademicCalendarRootEntry entry) {
	this.selectedRootEntryReference = entry == null ? null : new DomainReference<AcademicCalendarRootEntry>(entry);
    } 
       
    public AcademicCalendarEntry getTemplateEntry() {
	return templateEntryReference == null ? null : templateEntryReference.getObject();
    }
    
    public void setTemplateEntry(AcademicCalendarEntry entry) {
	this.templateEntryReference = entry == null ? null : new DomainReference<AcademicCalendarEntry>(entry);
    } 
    
    public AcademicCalendarEntry getEntry() {
	return entryReference == null ? null : entryReference.getObject();
    }
    
    public void setEntry(AcademicCalendarEntry academicCalendarEntry) {
	this.entryReference = academicCalendarEntry == null ? null : new DomainReference<AcademicCalendarEntry>(academicCalendarEntry);
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
