package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public abstract class TeacherCreditsFillingCE extends TeacherCreditsFillingCE_Base {
    
    protected TeacherCreditsFillingCE() {
	super();
    }
    
    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntry, AcademicCalendarEntry templateEntry) {
        
	throw new DomainException("error.unsupported.operation");
    }
           
    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
	throw new DomainException("error.unsupported.operation");
    }
          
    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	throw new DomainException("error.unsupported.operation");
    }
      
    public void edit(DateTime begin, DateTime end) {
	setTimeInterval(begin, end, getParentEntry());
    }
    
    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {	
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible(AcademicCalendarEntry entryToAdd) {	
	return false;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {	
	return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester();	
    }
    
    @Override
    public boolean isTeacherCreditsFilling() {
        return true;
    }

    public boolean containsNow() {
	return !getBegin().isAfterNow() && !getEnd().isBeforeNow();	
    }      
}
