package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionPeriodsNotClosedPublicProvider implements DataProvider {

    public Object provide(Object source) {

        /*
         * get executionPeriods after '2 Semestre 2005/2006'
         * 
         */ 
        final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readByNameAndExecutionYear("2 Semestre", "2005/2006");

        final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : ExecutionPeriod.readNotClosedPublicExecutionPeriods()) {
            if (executionPeriod.isAfterOrEquals(currentExecutionPeriod)) {
                result.add(executionPeriod);
            }
        }
        Collections.sort(result, new ReverseComparator());
        return result; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
