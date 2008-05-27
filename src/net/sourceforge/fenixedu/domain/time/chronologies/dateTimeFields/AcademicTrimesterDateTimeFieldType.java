package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicTrimestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicTrimesterDateTimeFieldType extends DateTimeFieldType {

    private static final AcademicTrimesterDateTimeFieldType ACADEMIC_TRIMESTER_TYPE;
    static {
	ACADEMIC_TRIMESTER_TYPE = new AcademicTrimesterDateTimeFieldType("academicSemester");
    }

    private AcademicTrimesterDateTimeFieldType(String name) {
	super(name);
    }

    public static DateTimeFieldType academicTrimester() {
	return ACADEMIC_TRIMESTER_TYPE;
    }

    @Override
    public DurationFieldType getDurationType() {
	return AcademicTrimestersDurationFieldType.academicTrimesters();
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
	if (chronology instanceof AcademicChronology) {
	    return ((AcademicChronology) chronology).academicYear();
	}
	throw unsupported();
    }

    @Override
    public DurationFieldType getRangeDurationType() {
	return null;
    }

    private UnsupportedOperationException unsupported() {
	return new UnsupportedOperationException(ACADEMIC_TRIMESTER_TYPE.getName() + " field is unsupported");
    }

}
