package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class OpenExecutionYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        final List<ExecutionYear> executionYears = ExecutionYear.readOpenExecutionYears();
        executionYears.add(ExecutionYear.readCurrentExecutionYear());

        Collections.sort(executionYears, new ReverseComparator());

        return executionYears;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
