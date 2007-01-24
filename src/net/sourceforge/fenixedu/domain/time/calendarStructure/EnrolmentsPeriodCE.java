package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class EnrolmentsPeriodCE extends EnrolmentsPeriodCE_Base {
      
    public EnrolmentsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end) {
	super();
	super.init(null, academicCalendarEntry, title, description, begin, end);
    }
        
    @Override
    public boolean isEnrolmentsPeriod() {
	return true;
    }

    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	if (parentEntry.isEnrolmentsPeriod() || parentEntry.isExamsPeriod() || parentEntry.isLessonsPerid()) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {	
	return false;
    }
}
