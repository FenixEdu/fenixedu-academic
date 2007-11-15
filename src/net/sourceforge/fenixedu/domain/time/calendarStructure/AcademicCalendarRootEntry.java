package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicCalendarRootEntry extends AcademicCalendarRootEntry_Base {

    private transient AcademicChronology academicChronology;
    
    public AcademicCalendarRootEntry(MultiLanguageString title, MultiLanguageString description, AcademicCalendarEntry templateCalendar) {
	super();	
	setRootDomainObjectForRootEntries(RootDomainObject.getInstance());
	setTitle(title);
	setDescription(description);	
	setTemplateEntry(templateCalendar);
    }
                 
    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
	    AcademicCalendarRootEntry rootEntryDestination, SeasonType seasonType, AcademicCalendarEntry templateEntry) {
	
	setTitle(title);
	setDescription(description);
	setTemplateEntry(templateEntry);	
	return this;
    }
    
    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
	super.setRootDomainObjectForRootEntries(null);	
        super.delete(rootEntry);
    }
    
    public AcademicChronology getAcademicChronology() {
	if (academicChronology == null) {
	    academicChronology = new AcademicChronology(this);
	}
	return academicChronology;
    }
    
    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
	if(!getChildEntries().isEmpty() && (!hasTemplateEntry() || !getTemplateEntry().equals(templateEntry))) {
	    throw new DomainException("error.RootEntry.invalid.templateEntry");    
	}	
        super.setTemplateEntry(templateEntry);
    }
              
    @Override
    public void setRootDomainObjectForRootEntries(RootDomainObject rootDomainObjectForRootEntries) {
        if(rootDomainObjectForRootEntries == null) {
            throw new DomainException("error.RootEntry.empty.rootDomainObject.to.academic.calendars");
        }
        super.setRootDomainObjectForRootEntries(rootDomainObjectForRootEntries);
    }
                  
    @Override
    public DateTime getBegin() {
	SortedSet<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);
	result.addAll(getChildEntries());
	return (result.isEmpty()) ? null : result.first().getBegin();
    }
           
    @Override
    public DateTime getEnd() {
        return null;
    }
           
    @Override
    public void setBegin(DateTime begin) {        
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void setEnd(DateTime end) {        
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {	
	throw new DomainException("error.unsupported.operation");	
    }
                
    @Override
    protected boolean areIntersectionsPossible() {	
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible() {	
	return true;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {	
	return false;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {	
	return true;
    }
    
    @Override
    public boolean isRoot() {
        return true;
    }     

    @Override
    public boolean containsInstant(final long instant) {
	return true;
    }   
}
