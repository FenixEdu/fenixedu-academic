package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class TeacherCreditsFillingCE extends TeacherCreditsFillingCE_Base {
    
    public TeacherCreditsFillingCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description,
	    DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry, CreditsEntity creditsEntity) {
	
        super();
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);                       
        setCreditsEntity(creditsEntity);
    }
    
    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntry, SeasonType seasonType, AcademicCalendarEntry templateEntry) {
        
	throw new DomainException("error.unsupported.operation");
    }
    
    public void edit(DateTime begin, DateTime end) {
	setTimeInterval(begin, end, getParentEntry());
    }
    
    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void setCreditsEntity(CreditsEntity entity) {
       
	if(entity == null) {
            throw new DomainException("error.TeacherCreditsFillingCE.empty.creditsEntity");
        }         
        
	for (AcademicCalendarEntry entry : getParentEntry().getChildEntriesWithTemplateEntries(TeacherCreditsFillingCE.class)) {
	    if(!entry.equals(this) && ((TeacherCreditsFillingCE)entry).getCreditsEntity().equals(entity)) {
		throw new DomainException("error.TeacherCreditsFilling.already.exists.entry");
	    }
	}
        
        super.setCreditsEntity(entity);
    }   
    
    @Override
    public CreditsEntity getCreditsEntity() {
        if(isVirtual()) {
            return ((TeacherCreditsFillingCE)getTemplateEntry()).getCreditsEntity();
        }
        return super.getCreditsEntity();
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
}
