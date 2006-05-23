package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionPeriodsNotClosedPublicProvider implements DataProvider {

    public Object provide(Object source) {
        final List<ExecutionPeriod> executionPeriods = ExecutionPeriod.readNotClosedPublicExecutionPeriods();
        Collections.sort(executionPeriods, new ReverseComparator());
        return executionPeriods; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
