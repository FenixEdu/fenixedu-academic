package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntryBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class CompetenceCourseForStudyPlanEntryProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
	final PhdStudyPlanEntryBean bean = (PhdStudyPlanEntryBean) source;

	return bean.getDegree() != null ? bean.getDegree().getLastActiveDegreeCurricularPlan().getCompetenceCourses()
		: Collections.EMPTY_LIST;

    }
}
