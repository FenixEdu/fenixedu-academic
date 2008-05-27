package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.time.AcademicPeriodType;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicYearsDurationFieldType;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicYears extends AcademicPeriod {

    protected AcademicYears(int period, String name) {
	super(period, name);
    }

    @Override
    public DurationFieldType getFieldType() {
	return AcademicYearsDurationFieldType.academicYears();
    }

    @Override
    public PeriodType getPeriodType() {
	return AcademicPeriodType.academicYears();
    }

    @Override
    public float getWeight() {
	return getValue();
    }

}
