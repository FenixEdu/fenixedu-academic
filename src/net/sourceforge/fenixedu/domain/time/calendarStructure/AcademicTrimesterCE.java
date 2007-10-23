package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicTrimesterCE extends AcademicTrimesterCE_Base {

    public AcademicTrimesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);
    } 
    
    private AcademicTrimesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarEntry templateEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, parentEntry.getRootEntry());
	setTemplateEntry(templateEntry);
    } 
    
    private AcademicTrimesterCE(AcademicCalendarEntry parentEntry, AcademicTrimesterCE academicTrimesterCE) {
	super();
	super.initVirtualOrRedefinedEntry(parentEntry, academicTrimesterCE);
    }

    @Override
    public boolean isAcademicTrimester() {
	return true;
    }
    
    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicYear() && !parentEntry.isAcademicSemester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {	
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
	    return new AcademicTrimesterCE(parentEntry, this);
	} else {
	    return new AcademicTrimesterCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd(), this);
	}
    }
}
