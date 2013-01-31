package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class DegreesProviderForStudyPlanEntry extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object current) {

		final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

		result.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA,
				DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA, DegreeType.BOLONHA_DEGREE,
				DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE));

		return result;

	}
}
