package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<DegreeCurricularPlan> result = new TreeSet<DegreeCurricularPlan>(
		DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	SearchStudentsByCurricularCourseParametersBean bean = ((SearchStudentsByCurricularCourseParametersBean) source);
	final ExecutionYear executionYear = bean.getExecutionYear();

	if (executionYear != null) {
	    final Set<Degree> degrees = bean.getAdministratedDegrees();

	    for (DegreeCurricularPlan plan : executionYear.getDegreeCurricularPlans()) {
		if (degrees.contains(plan.getDegree())) {
		    result.add(plan);
		}
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
