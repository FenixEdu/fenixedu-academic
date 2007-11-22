package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class GradeSubmissionInNormalSeasonCE extends GradeSubmissionInNormalSeasonCE_Base {
    
    public GradeSubmissionInNormalSeasonCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);		
    }

    private GradeSubmissionInNormalSeasonCE(AcademicCalendarEntry academicCalendarEntry, GradeSubmissionCE gradeSubmissionCE) {	
	super();	
	super.initVirtualEntry(academicCalendarEntry, gradeSubmissionCE);		
    }
    
    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new GradeSubmissionInNormalSeasonCE(parentEntry, this);
    }  
}
