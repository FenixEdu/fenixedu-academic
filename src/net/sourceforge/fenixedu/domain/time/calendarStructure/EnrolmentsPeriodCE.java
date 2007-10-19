package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class EnrolmentsPeriodCE extends EnrolmentsPeriodCE_Base {
      
    public EnrolmentsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end) {
	
	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end);
    }
        
    private EnrolmentsPeriodCE(AcademicCalendarEntry parentEntry, EnrolmentsPeriodCE enrolmentsPeriodCE) {
	super();
	super.initVirtualEntry(parentEntry, enrolmentsPeriodCE);
    }

    @Override
    public boolean isEnrolmentsPeriod() {
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
	    return new EnrolmentsPeriodCE(parentEntry, this);
	} else {
	    return new EnrolmentsPeriodCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd());   
	}
    }
}
