package net.sourceforge.fenixedu.domain.time.calendarStructure;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.DateTime;

public class GradeSubmissionInSpecialSeasonCE extends GradeSubmissionInSpecialSeasonCE_Base {
    
    public GradeSubmissionInSpecialSeasonCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);		
    }
    
    private GradeSubmissionInSpecialSeasonCE(AcademicCalendarEntry academicCalendarEntry, GradeSubmissionCE gradeSubmissionCE) {	
	super();	
	super.initVirtualEntry(academicCalendarEntry, gradeSubmissionCE);		
    }
    
    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new GradeSubmissionInSpecialSeasonCE(parentEntry, this);
    }  
}
