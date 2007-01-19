package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicTrimesterCE extends AcademicTrimesterCE_Base {

    public AcademicTrimesterCE(AcademicCalendar academicCalendar, AcademicCalendarEntry parentEntry,
	    MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end) {
	super();
	super.init(academicCalendar, parentEntry, title, description, begin, end);
    } 
    
    @Override
    public boolean isAcademicTrimester() {
	return true;
    }
    
    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	if (parentEntry.isAcademicYear() || parentEntry.isAcademicSemester()) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {	
	return false;
    }
}
