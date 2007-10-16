package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class GradeSubmissionCE extends GradeSubmissionCE_Base {
    
    public GradeSubmissionCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, SeasonType seasonType) {
	super();
	super.init(null, academicCalendarEntry, title, description, begin, end);	
	setSeasonType(seasonType);
    }

    @Override
    public void setSeasonType(SeasonType seasonType) {
        if(seasonType == null) {
            throw new DomainException("error.GradeSubmissionCE.empty.season.type");
        }
        super.setSeasonType(seasonType);
    }
    
    @Override
    public boolean areIntersectionsPossible() {	
	return true;
    }

    @Override
    public boolean areOutOfBoundsPossible() {
	return true;
    }

    @Override
    public boolean isGradeSubmissionPeriod() {
	return true;
    }
    
    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {	
	return false;
    }

    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	if (parentEntry.isEnrolmentsPeriod() || parentEntry.isExamsPeriod() || parentEntry.isLessonsPerid() 
		|| parentEntry.isGradeSubmissionPeriod()) {
	    return true;
	}
	return false;	
    } 
}
