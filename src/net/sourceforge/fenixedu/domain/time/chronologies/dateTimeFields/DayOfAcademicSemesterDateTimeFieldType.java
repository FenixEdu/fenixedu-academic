package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class DayOfAcademicSemesterDateTimeFieldType extends DateTimeFieldType {

    public static final DayOfAcademicSemesterDateTimeFieldType DAY_OF_ACADEMIC_SEMESTER_TYPE;

    static {
	DAY_OF_ACADEMIC_SEMESTER_TYPE = new DayOfAcademicSemesterDateTimeFieldType(
		"dayOfAcademicSemester", DurationFieldType.days(),
		AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE);
    }

    private DayOfAcademicSemesterDateTimeFieldType(String name, DurationFieldType unitType,
	    DurationFieldType rangeType) {
	super(name);
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).dayOfAcademicSemester();
	}
	throw unsupported();
    }
    
    @Override
    public DurationFieldType getDurationType() {
	return DurationFieldType.days();
    }

    @Override
    public DurationFieldType getRangeDurationType() {
	return AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE;
    }
    
    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(DAY_OF_ACADEMIC_SEMESTER_TYPE + " field is unsupported");
    }
}
