package org.fenixedu.academic.domain.time.calendarStructure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicDays extends AcademicPeriod {

    protected AcademicDays(int period, String name) {
        super(period, name);
    }

    @Override
    public float getWeight() {
        return getValue() / 365f;
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.days();
    }

    @Override
    public AcademicPeriod getPossibleChild() {
        return null;
    }

    @Override
    public Collection<AcademicPeriod> getPossibleChilds() {
        final Set<AcademicPeriod> result = new HashSet<>();

//        if (getValue() > 1) {
//            result.add(AcademicPeriod.DAY);
//        }

        return result;
    }

}
