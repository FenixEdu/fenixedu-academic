package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ThesisFilterBean;

public class DepartmentDegreesWithDissertationProvider extends DegreesWithDissertationProvider {

    @Override
    protected Collection<Degree> getDegrees(Object source) {
	ThesisFilterBean bean = (ThesisFilterBean) source;
	return bean.getDegreeOptions();
    }

}
