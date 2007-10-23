package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicSemesterCE extends AcademicSemesterCE_Base {

    public AcademicSemesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);
    }    
    
    private AcademicSemesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarEntry templateEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, parentEntry.getRootEntry());
	setTemplateEntry(templateEntry);
    }    

    private AcademicSemesterCE(AcademicCalendarEntry parentEntry, AcademicSemesterCE academicSemesterCE) {
	super();
	super.initVirtualOrRedefinedEntry(parentEntry, academicSemesterCE);
    }

    @Override
    public boolean isAcademicSemester() {
	return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicYear();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
	if(childEntry.isAcademicTrimester()) {
	    return getChildEntries(AcademicTrimesterCE.class).size() >= 2;
	}	
	return false;
    }

    @Override
    protected boolean areIntersectionsPossible() {	
	return true;
    }

    @Override
    protected boolean areOutOfBoundsPossible() { 
	return true;
    }

    @Override
    protected AcademicCalendarEntry makeAnEntryCopyInDifferentCalendar(AcademicCalendarEntry parentEntry, boolean virtual) {
	if(virtual) {
	    return new AcademicSemesterCE(parentEntry, this);
	} else {
	    return new AcademicSemesterCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd(), this);
	}
    }              
}
