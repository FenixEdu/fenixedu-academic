package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicSemestersDurationFieldType extends DurationFieldType {

    public static final AcademicSemestersDurationFieldType ACADEMIC_SEMESTERS_TYPE;
    
    static {
	ACADEMIC_SEMESTERS_TYPE = new AcademicSemestersDurationFieldType("academicSemesters");
    }
    
    protected AcademicSemestersDurationFieldType(String name) {
	super(name);	
    }

    @Override
    public DurationField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).academicSemesters();
	}
	return null;
    }

}
