package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasDegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreeForExecutionYearAndDegreeTypeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();

		final HasExecutionYear hasExecutionYear = (HasExecutionYear) source;
		final HasDegreeType hasDegreeType = (HasDegreeType) source;
		final ExecutionYear executionYear = hasExecutionYear.getExecutionYear();
		if (executionYear != null) {
			final DegreeType degreeType = hasDegreeType.getDegreeType();
			for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
				if (degreeType == null || match(degreeType, executionDegree)) {
					executionDegrees.add(executionDegree);
				}
			}
		}

		Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

		return executionDegrees;
	}

	private boolean match(final DegreeType degreeType, final ExecutionDegree executionDegree) {
		return executionDegree.getDegreeType() == degreeType;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
