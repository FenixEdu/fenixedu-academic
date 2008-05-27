package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.time.AcademicPeriodType;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicTrimestersDurationFieldType;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicTrimesters extends AcademicPeriod {

    protected AcademicTrimesters(int period, String name) {
	super(period, name);
    }

    @Override
    public float getWeight() {
	return getValue() / 4f;
    }

    @Override
    public DurationFieldType getFieldType() {
	return AcademicTrimestersDurationFieldType.academicTrimesters();
    }

    @Override
    public PeriodType getPeriodType() {
	return AcademicPeriodType.academicTrimesters();
    }

}
