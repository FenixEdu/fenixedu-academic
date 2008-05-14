package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class AllExecutionPeriodsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>(RootDomainObject.getInstance()
		.getExecutionPeriods());
	Collections.sort(executionSemesters, new ReverseComparator());
	return executionSemesters;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
