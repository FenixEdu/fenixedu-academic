package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsNotClosedPublicProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

	for (final ExecutionSemester executionSemester : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionSemester.isAfterOrEquals(ExecutionSemester.readMarkSheetManagmentExecutionPeriod())) {
		result.add(executionSemester);
	    }
	}

	Collections.sort(result, new ReverseComparator());
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
