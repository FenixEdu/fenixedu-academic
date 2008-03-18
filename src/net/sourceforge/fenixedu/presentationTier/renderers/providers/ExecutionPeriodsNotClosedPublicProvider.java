package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionPeriodsNotClosedPublicProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();

	for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionPeriod.isAfterOrEquals(ExecutionPeriod.readMarkSheetManagmentExecutionPeriod())) {
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
