package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;
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
    public void delete(AcademicCalendarRootEntry rootEntry) {		
	ExecutionPeriod executionPeriod = ExecutionPeriod.getExecutionPeriod(this);
	executionPeriod.delete();	
	super.delete(rootEntry);
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
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible() { 
	return false;
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
	List<AcademicCalendarEntry> list = academicYearCE.getChildEntriesWithTemplateEntries(academicYearCE.getBegin(), getBegin().minusDays(1), getClass());
	return list.size() + 1;	
    }  
}
