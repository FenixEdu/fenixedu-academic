package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class TeacherCreditsFillingForDepartmentAdmOfficeCE extends TeacherCreditsFillingForDepartmentAdmOfficeCE_Base {
    
    public TeacherCreditsFillingForDepartmentAdmOfficeCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {
	
        super();
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);                               
    }      
}
