package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Over23IndividualCandidacyDegreesProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		final Set<AcademicProgram> programs =
				AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
						AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

		return Collections2.filter(getDegrees(source), new Predicate<Degree>() {
			@Override
			public boolean apply(Degree degree) {
				return programs.contains(degree);
			}
		});
	}

	private Collection<Degree> getDegrees(Object source) {
		return Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
