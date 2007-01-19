package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicSemesterDateTimeFieldType extends DateTimeFieldType {

    public static final AcademicSemesterDateTimeFieldType ACADEMIC_SEMESTER_YEAR;

    static {
	ACADEMIC_SEMESTER_YEAR = new AcademicSemesterDateTimeFieldType("academicSemester");
    }

    protected AcademicSemesterDateTimeFieldType(String name) {
	super(name);
    }

    @Override
    public DurationFieldType getDurationType() {
	return AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE;
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).academicSemester();
	}
	throw unsupported();	
    }

    @Override
    public DurationFieldType getRangeDurationType() {	
	return null;
    }
    
    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(ACADEMIC_SEMESTER_YEAR + " field is unsupported");
    }
}
