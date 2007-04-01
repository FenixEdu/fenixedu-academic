package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionPeriodsNotClosedPublicProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();

	final String executionPeriodIDString = PropertiesManager.getProperty("object.id.execution.period.for.from.mark.sheet.managment");
	final ExecutionPeriod fromExecutionPeriod;
	if (executionPeriodIDString == null || executionPeriodIDString.length() == 0) {
	    fromExecutionPeriod = null;
	} else {
	    fromExecutionPeriod = RootDomainObject.getInstance().readExecutionPeriodByOID(Integer.valueOf(executionPeriodIDString));
	}

        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
            if (executionPeriod.isAfterOrEquals(fromExecutionPeriod)) {
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
