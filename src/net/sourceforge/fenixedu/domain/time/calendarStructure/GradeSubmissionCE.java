package net.sourceforge.fenixedu.domain.time.calendarStructure;


public abstract class GradeSubmissionCE extends GradeSubmissionCE_Base {

    protected GradeSubmissionCE() {
	super();	
    }  

    @Override
    public boolean isGradeSubmissionPeriod() {
	return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();	
    } 

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {	
	return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {        
        return false;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {	
	return true;
    }   
    
    @Override
    protected boolean associatedWithDomainEntities() {       
        return false;
    }
}
