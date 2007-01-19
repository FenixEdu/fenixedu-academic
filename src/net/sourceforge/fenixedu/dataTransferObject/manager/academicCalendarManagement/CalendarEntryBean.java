package net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;

public class CalendarEntryBean implements Serializable {

    private DomainReference<AcademicCalendarEntry> parentEntryReference;
    
    private DomainReference<AcademicCalendar> academicCalendarReference;
    
    private Class type;
    
    
    public CalendarEntryBean(AcademicCalendarEntry academicCalendarEntry) {
	setParentEntry(academicCalendarEntry);
    }
    
    public CalendarEntryBean(AcademicCalendar academicCalendar) {
	setAcademicCalendar(academicCalendar);
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
}
