package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsFrom1Sem0607 implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	/*
	 * get executionPeriods after '1 Semestre 2006/2007'
	 */
	final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readBySemesterAndExecutionYear(1, "2006/2007");

	final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
	for (final ExecutionSemester executionSemester : ExecutionSemester.readNotClosedPublicExecutionPeriods()) {
	    if (executionSemester.isAfterOrEquals(currentExecutionPeriod)) {
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
