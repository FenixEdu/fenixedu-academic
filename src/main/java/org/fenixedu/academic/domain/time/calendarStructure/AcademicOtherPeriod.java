package org.fenixedu.academic.domain.time.calendarStructure;

import java.util.Collection;
import java.util.Collections;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicOtherPeriod extends AcademicPeriod {

    protected AcademicOtherPeriod(int period, String name) {
        super(period, name);
    }

    @Override
    public float getWeight() {
        return 0.01f;
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.millis(); //for compatiblity reasons
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.millis();//for compatiblity reasons
    }

    @Override
    public AcademicPeriod getPossibleChild() {
        return null;
    }

    @Override
    public Collection<AcademicPeriod> getPossibleChilds() {
        return Collections.emptySet();
    }

}
