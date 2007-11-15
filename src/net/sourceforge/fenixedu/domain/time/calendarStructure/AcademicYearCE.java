package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class AcademicYearCE extends AcademicYearCE_Base {

    public AcademicYearCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(parentEntry, title, description, begin, end, rootEntry);	
	createExecutionYear();			
    }

    private AcademicYearCE(AcademicCalendarEntry parentEntry, AcademicYearCE academicYearCE) {
	super();
	super.initVirtualEntry(parentEntry, academicYearCE);
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {	
	ExecutionYear executionYear = ExecutionYear.getExecutionYear(this);
	executionYear.delete();	
	super.delete(rootEntry);
    }

    @Override
    protected void afterRedefineEntry() {
	createExecutionYear();
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
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible() {	
	return false;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new AcademicYearCE(parentEntry, this);	
    }

    private void createExecutionYear() {	
	ExecutionYear executionYear = ExecutionYear.readBy(getBegin().toYearMonthDay(), getEnd().toYearMonthDay());
	if(executionYear == null) {
	    new ExecutionYear(new AcademicInterval(this));	
	}
    }  

    @Override
    public int getAcademicSemesterOfAcademicYear(AcademicChronology academicChronology) {
	throw new DomainException("error.invalid.operation");
    }   
}
