package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class ExamsPeriodCE extends ExamsPeriodCE_Base {
    
    public ExamsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, SeasonType seasonType) {
	
	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end);
	setSeasonType(seasonType);
    }

    private ExamsPeriodCE(AcademicCalendarEntry parentEntry, ExamsPeriodCE examsPeriodCE) {
	super();
	super.initVirtualEntry(parentEntry, examsPeriodCE);
    }

    @Override
    public boolean isExamsPeriod() {
	return true;
    }

    @Override
    public void setSeasonType(SeasonType seasonType) {
        if(seasonType == null) {
            throw new DomainException("error.ExamsPeriodCE.empty.season.type");
        }
        super.setSeasonType(seasonType);
    }
    
    @Override
    public SeasonType getSeasonType() {
        if(isVirtual()) {
            return ((ExamsPeriodCE)getTemplateEntry()).getSeasonType();
        }
        return super.getSeasonType();
    }
    
    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
	return false;
    }

    @Override
    protected boolean areIntersectionsPossible() {	
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible() {
	return false;
    }

    @Override
    protected AcademicCalendarEntry makeAnEntryCopyInDifferentCalendar(AcademicCalendarEntry parentEntry, boolean virtual) {
	if(virtual) {
	    return new ExamsPeriodCE(parentEntry, this);
	} else {
	    return new ExamsPeriodCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd(), getSeasonType());
	}
    }
}
