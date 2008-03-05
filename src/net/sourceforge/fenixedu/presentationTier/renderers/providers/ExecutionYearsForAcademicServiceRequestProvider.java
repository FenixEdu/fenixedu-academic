package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionYearsForAcademicServiceRequestProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionYear> result = ExecutionYear.readNotClosedExecutionYears();
	Collections.sort(result, ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
