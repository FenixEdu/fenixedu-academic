package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator;
import net.sourceforge.fenixedu.renderers.DataProvider;

public class DestinationDegreeModulesPreviousCourseGroupForStudentEquivalencePlanEntryCreatorProvider
	extends DestinationDegreeModulesPreviousCourseGroupForEquivalencePlanEntryCreatorProvider
	implements DataProvider {

    @Override
    protected DegreeCurricularPlan getDegreeCurricularPlan(Object source) {
	return ((StudentEquivalencyPlanEntryCreator) source).getDegreeCurricularPlanEquivalencePlan()
		.getDegreeCurricularPlan();
    }

}
