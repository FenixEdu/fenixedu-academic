package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class LessonsPeriodCE extends LessonsPeriodCE_Base {
    
    public LessonsPeriodCE(AcademicCalendar academicCalendar, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end) {
	super();
	super.init(academicCalendar, null, title, description, begin, end);
    }
    
    @Override
    public boolean isLessonsPerid() {
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
