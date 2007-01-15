package net.sourceforge.fenixedu.domain.time.durationFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicYearsDurationFieldType extends DurationFieldType {

    public static final AcademicYearsDurationFieldType ACADEMIC_YEARS_TYPE;
    
    static {
	ACADEMIC_YEARS_TYPE = new AcademicYearsDurationFieldType("academicYears");
    }
    
    protected AcademicYearsDurationFieldType(String name) {
	super(name);
    }

    @Override
    public DurationField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).academicYears();
	}
	return null;
    }
}
