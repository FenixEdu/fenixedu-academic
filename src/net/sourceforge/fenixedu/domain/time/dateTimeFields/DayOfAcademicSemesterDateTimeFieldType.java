package net.sourceforge.fenixedu.domain.time.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.durationFields.AcademicSemestersDurationFieldType;

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
	return null;	
    }
    
    @Override
    public DurationFieldType getDurationType() {
	return DurationFieldType.days();
    }

    @Override
    public DurationFieldType getRangeDurationType() {
	return AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE;
    }
}
