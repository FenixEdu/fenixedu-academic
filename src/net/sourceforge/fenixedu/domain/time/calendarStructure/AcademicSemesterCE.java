package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicSemesterCE extends AcademicSemesterCE_Base {

    public AcademicSemesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, 
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);
	createNewExecutionPeriod();
    }       

    private AcademicSemesterCE(AcademicCalendarEntry parentEntry, AcademicSemesterCE academicSemesterCE) {
	super();
	super.initVirtualEntry(parentEntry, academicSemesterCE);
    }
    
    @Override
    protected void afterRedefineEntry() {
	createNewExecutionPeriod();
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
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new AcademicSemesterCE(parentEntry, this);	
    }       

    private void createNewExecutionPeriod() {		
	AcademicYearCE academicYear = (AcademicYearCE) getParentEntry().getOriginalTemplateEntry();		
	ExecutionYear executionYear = ExecutionYear.getExecutionYear(academicYear);	    
	new ExecutionPeriod(executionYear, new AcademicInterval(this), getTitle().getContent());
    }

    @Override
    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
	final AcademicYearCE academicYearCE = (AcademicYearCE) academicChronology.findParentOf(this);
	return academicYearCE.countAcademicSemesterOfAcademicYear(this, 1, academicYearCE);
    }

}
