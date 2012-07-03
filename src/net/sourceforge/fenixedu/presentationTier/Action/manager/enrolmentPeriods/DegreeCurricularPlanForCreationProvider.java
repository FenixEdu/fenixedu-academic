package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolmentPeriods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodManagementBean;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlanForCreationProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {

	EnrolmentPeriodManagementBean bean = (EnrolmentPeriodManagementBean) source;

	List<DegreeCurricularPlan> activeDegreeCurricularPlans = readActiveDegreeCurricularPlanWithoutPeriod(bean);

	return activeDegreeCurricularPlans;
    }

    private List<DegreeCurricularPlan> readActiveDegreeCurricularPlanWithoutPeriod(final EnrolmentPeriodManagementBean bean) {

	DegreeType degreeType = bean.getDegreeType();
	EnrolmentPeriodType type = bean.getType();

	if (degreeType == null || type == null) {
	    return new ArrayList<DegreeCurricularPlan>();
	}

	ExecutionSemester executionSemester = bean.getExecutionSemester();
	Collection<ExecutionDegree> executionDegrees = executionSemester.getExecutionYear().getExecutionDegreesByType(degreeType);

	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (ExecutionDegree executionDegree : executionDegrees) {
	    DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	    List<EnrolmentPeriod> enrolmentPeriods = degreeCurricularPlan.getEnrolmentPeriods();

	    boolean hasPeriod = false;
	    for (EnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
		if (type.is(enrolmentPeriod) && enrolmentPeriod.getExecutionPeriod() == executionSemester) {
		    hasPeriod = true;
		}
	    }

	    if (!hasPeriod) {
		result.add(degreeCurricularPlan);
	    }
	}

	return result;
    }

}
