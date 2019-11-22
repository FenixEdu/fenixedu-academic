package org.fenixedu.academic.domain.time.calendarStructure;

import java.util.Collection;
import java.util.Collections;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicMonths extends AcademicPeriod {

    protected AcademicMonths(int period, String name) {
        super(period, name);
    }

    @Override
    public float getWeight() {
        return getValue() / 12f;
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.months();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.months();
    }

    @Override
    public AcademicPeriod getPossibleChild() {
        return null;
    }

    @Override
    public Collection<AcademicPeriod> getPossibleChilds() {
//        final Set<AcademicPeriod> result = new HashSet<>();
//
//        if (getValue() > 1) {
//            result.add(AcademicPeriod.MONTH);
//        }
//
//        result.add(AcademicPeriod.WEEK);
//        result.add(AcademicPeriod.DAY);
//
//        return result;

        return Collections.emptySet();
    }

}
