package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class AllExecutionPeriodsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<ExecutionPeriod> executionPeriods = new ArrayList<ExecutionPeriod>(RootDomainObject.getInstance()
		.getExecutionPeriods());
	Collections.sort(executionPeriods, new ReverseComparator());
	return executionPeriods;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
