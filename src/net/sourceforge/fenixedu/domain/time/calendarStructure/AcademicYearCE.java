package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicYearCE extends AcademicYearCE_Base {
    
    public AcademicYearCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);
    } 
    
    private AcademicYearCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarEntry templateEntry) {
	
	super();
	super.initEntry(parentEntry, title, description, begin, end, parentEntry.getRootEntry());
	setTemplateEntry(templateEntry);
    }
    
    private AcademicYearCE(AcademicCalendarEntry parentEntry, AcademicYearCE academicYearCE) {
	super();
	super.initVirtualOrRedefinedEntry(parentEntry, academicYearCE);
    }

    @Override
    public boolean isAcademicYear() {
	return true;
    } 
    
    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {	
	return !parentEntry.isRoot();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {	
	if(childEntry.isAcademicSemester()) {
	    return getChildEntries(AcademicSemesterCE.class).size() >= 2;
	}
	if(childEntry.isAcademicTrimester()) {
	    return getChildEntries(AcademicTrimesterCE.class).size() >= 4;
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
	    return new AcademicYearCE(parentEntry, this);
	} else {
	    return new AcademicYearCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd(), this);
	}
    }    
}
