package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class DegreeProvider extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object value) {
		DegreeFilterBean bean = (DegreeFilterBean) source;

		return Degree.readAllByDegreeType(bean.getDegreeType());
	}

}
