package net.sourceforge.fenixedu.domain.time.calendarStructure;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.DateTime;

public class LessonsPeriodCE extends LessonsPeriodCE_Base {

    public LessonsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 	    
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
    }

    private LessonsPeriodCE(AcademicCalendarEntry entry, LessonsPeriodCE lessonsPeriodCE) {
	super();
	super.initVirtualEntry(entry, lessonsPeriodCE);
    }

    @Override
    public boolean isLessonsPerid() {
	return true;
    }  

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
	return true;
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
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new LessonsPeriodCE(parentEntry, this);	
    }

    @Override
    protected boolean associatedWithDomainEntities() {	
	return false;
    }
}
