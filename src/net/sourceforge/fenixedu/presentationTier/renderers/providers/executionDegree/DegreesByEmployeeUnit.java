package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesByEmployeeUnit implements DataProvider {
	@Override
	public Object provide(Object source, Object currentValue) {
		final List<Degree> result = new ArrayList<Degree>();
		for (Degree degree : getDegrees()) {
			final DegreeType degreeType = degree.getDegreeType();
			if (degreeType.canCreateStudent() && !degreeType.canCreateStudentOnlyWithCandidacy()) {
				result.add(degree);
			}
		}

		Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

		return result;
	}

	protected Collection<Degree> getDegrees() {
		return Degree.readNotEmptyDegrees();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
}
