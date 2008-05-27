package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.time.AcademicPeriodType;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicSemesters extends AcademicPeriod {

    protected AcademicSemesters(int period, String name) {
	super(period, name);
    }

    @Override
    public DurationFieldType getFieldType() {
	return AcademicSemestersDurationFieldType.academicSemesters();
    }

    @Override
    public PeriodType getPeriodType() {
	return AcademicPeriodType.academicSemesters();
    }

    @Override
    public float getWeight() {
	return getValue() / 2f;
    }

}
