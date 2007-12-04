package net.sourceforge.fenixedu.domain.time.calendarStructure;


public abstract class ExamsPeriodCE extends ExamsPeriodCE_Base {

    protected ExamsPeriodCE() {
	super();	
    }

    @Override
    public boolean isExamsPeriod() {
	return true;
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
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {	
	return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {      
        return false;
    } 
}
