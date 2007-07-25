package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.util.search.StudentSearchBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeCurricularPlanForStudentEquivalenciesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final StudentSearchBean studentSearchBean = (StudentSearchBean) source;
	return studentSearchBean.getDegreeCurricularPlan() == studentSearchBean.getOldDegreeCurricularPlan() ? getAllDegreeCurricularPlans()
		: getDestinationsDegreeCurricularPlans(studentSearchBean.getOldDegreeCurricularPlan());
    }

    private List<DegreeCurricularPlan> getDestinationsDegreeCurricularPlans(DegreeCurricularPlan oldDegreeCurricularPlan) {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan : oldDegreeCurricularPlan
		.getTargetEquivalencePlans()) {
	    result.add(degreeCurricularPlanEquivalencePlan.getDegreeCurricularPlan());
	}

	return result;

    }

    private List<DegreeCurricularPlan> getAllDegreeCurricularPlans() {
	final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
	degreeTypes.add(DegreeType.BOLONHA_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	return new ArrayList<DegreeCurricularPlan>(DegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
