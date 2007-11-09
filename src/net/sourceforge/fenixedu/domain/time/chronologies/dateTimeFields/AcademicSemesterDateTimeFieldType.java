package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicSemesterDateTimeFieldType extends DateTimeFieldType {

    private static final AcademicSemesterDateTimeFieldType ACADEMIC_SEMESTER_TYPE;
    static {
	ACADEMIC_SEMESTER_TYPE = new AcademicSemesterDateTimeFieldType("academicSemester");
    }

    protected AcademicSemesterDateTimeFieldType(String name) {
	super(name);
    }

    public static DateTimeFieldType academicSemester() {
	return ACADEMIC_SEMESTER_TYPE;
    }
    
    @Override
    public DurationFieldType getDurationType() {
	return AcademicSemestersDurationFieldType.academicSemesters();
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
        return new UnsupportedOperationException(ACADEMIC_SEMESTER_TYPE + " field is unsupported");
    }
}
