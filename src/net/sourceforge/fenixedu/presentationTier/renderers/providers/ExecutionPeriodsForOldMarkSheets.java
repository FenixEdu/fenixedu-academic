package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsForOldMarkSheets implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

	for (final ExecutionSemester executionSemester : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
	    if (executionSemester.isBefore(ExecutionSemester.readMarkSheetManagmentExecutionPeriod())) {
		result.add(executionSemester);
	    }
	}

	Collections.sort(result, new ReverseComparator());
	return result;
    }

}
