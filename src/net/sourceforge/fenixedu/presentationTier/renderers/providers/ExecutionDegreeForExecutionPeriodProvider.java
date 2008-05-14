package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionDegreeForExecutionPeriodProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();

	final HasExecutionSemester hasExecutionSemester = (HasExecutionSemester) source;
	final ExecutionSemester executionPeriod = hasExecutionSemester.getExecutionPeriod();
	if (executionPeriod != null) {
	    final ExecutionYear executionYear = executionPeriod.getExecutionYear();
	    executionDegrees.addAll(executionYear.getExecutionDegreesSet());
	}

	Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

	return executionDegrees;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
