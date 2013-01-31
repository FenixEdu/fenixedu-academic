package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class DegreesProviderForStudyPlan extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object current) {
		final PhdStudyPlanBean bean = (PhdStudyPlanBean) source;

		final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

		result.add(bean.getProcess().getPhdProgram().getDegree());
		result.add(Degree.readEmptyDegree());

		return result;
	}
}
