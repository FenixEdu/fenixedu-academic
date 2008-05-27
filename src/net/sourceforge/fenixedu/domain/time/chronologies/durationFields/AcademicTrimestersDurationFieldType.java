package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicTrimestersDurationFieldType extends DurationFieldType {

    public static final AcademicTrimestersDurationFieldType ACADEMIC_TRIMESTERS_TYPE;
    static {
	ACADEMIC_TRIMESTERS_TYPE = new AcademicTrimestersDurationFieldType("academicTrimesters");
    }

    protected AcademicTrimestersDurationFieldType(String name) {
	super(name);
    }

    public static DurationFieldType academicTrimesters() {
	return ACADEMIC_TRIMESTERS_TYPE;
    }

    @Override
    public DurationField getField(Chronology chronology) {
	if (chronology instanceof AcademicChronology) {
	    return ((AcademicChronology) chronology).academicTrimesters();
	}
	return null;
    }

}
