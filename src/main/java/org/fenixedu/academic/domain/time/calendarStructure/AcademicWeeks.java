package org.fenixedu.academic.domain.time.calendarStructure;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicWeeks extends AcademicPeriod {

    protected AcademicWeeks(int period, String name) {
        super(period, name);
    }

    @Override
    public float getWeight() {
        return getValue() / 52f;
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.weeks();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.weeks();
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
//            result.add(AcademicPeriod.WEEK);
//        }
//
//        result.add(AcademicPeriod.DAY);
//
//        return result;
        
        return Collections.emptySet();
    }

}
