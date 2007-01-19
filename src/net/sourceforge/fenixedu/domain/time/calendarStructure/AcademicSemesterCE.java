package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicSemesterCE extends AcademicSemesterCE_Base {

    public AcademicSemesterCE(AcademicCalendar academicCalendar, AcademicCalendarEntry parentEntry,
	    MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end) {
	super();
	super.init(academicCalendar, parentEntry, title, description, begin, end);
    }    

    @Override
    public boolean isAcademicSemester() {
	return true;
    }

    @Override
    public boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	if (parentEntry.isAcademicYear()) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry) {
	if(childEntry.isAcademicTrimester()) {
	    return (getSubEntries(AcademicTrimesterCE.class).size() >= 2) ? true : false;
	}
	return false;
    }
}
