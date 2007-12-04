package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicTrimesterCE extends AcademicTrimesterCE_Base {

    public AcademicTrimesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);
    } 
   
    private AcademicTrimesterCE(AcademicCalendarEntry parentEntry, AcademicTrimesterCE academicTrimesterCE) {
	super();
	super.initVirtualEntry(parentEntry, academicTrimesterCE);
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
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) { 
	return true;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return true;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new AcademicTrimesterCE(parentEntry, this);	
    }
}
