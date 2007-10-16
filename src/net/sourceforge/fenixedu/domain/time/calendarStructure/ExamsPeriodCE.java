package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class ExamsPeriodCE extends ExamsPeriodCE_Base {
    
    public ExamsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, SeasonType seasonType) {
	super();
	super.init(null, academicCalendarEntry, title, description, begin, end);
	setSeasonType(seasonType);
    }
    
    @Override
    public void setSeasonType(SeasonType seasonType) {
        if(seasonType == null) {
            throw new DomainException("error.ExamsPeriodCE.empty.season.type");
        }
        super.setSeasonType(seasonType);
    }
    
    @Override
    public boolean isExamsPeriod() {
	return true;
    }

    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	if (parentEntry.isEnrolmentsPeriod() || parentEntry.isExamsPeriod() || parentEntry.isLessonsPerid()
		|| parentEntry.isGradeSubmissionPeriod()) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {
	return false;
    }

    @Override
    public boolean areIntersectionsPossible() {	
	return true;
    }

    @Override
    public boolean areOutOfBoundsPossible() {
	return true;
    }
}
