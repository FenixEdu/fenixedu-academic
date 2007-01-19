package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicYearCE extends AcademicYearCE_Base {
    
    public AcademicYearCE(AcademicCalendar academicCalendar, AcademicCalendarEntry parentEntry,
	    MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end) {
	super();
	super.init(academicCalendar, parentEntry, title, description, begin, end);
    } 
    
    @Override
    public boolean isAcademicYear() {
	return true;
    }    

    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {	
	return true;
    }

    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {	
	if(childEntry.isAcademicSemester()) {
	    return (getSubEntries(AcademicSemesterCE.class).size() >= 2) ? true : false;
	}
	if(childEntry.isAcademicTrimester()) {
	    return (getSubEntries(AcademicTrimesterCE.class).size() >= 4) ? true : false;
	}
	return false;
    }    
}
