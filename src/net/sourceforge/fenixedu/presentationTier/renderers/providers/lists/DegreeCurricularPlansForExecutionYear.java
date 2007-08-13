package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeCurricularPlansForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<DegreeCurricularPlan> result = new TreeSet<DegreeCurricularPlan>(
		DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	final ExecutionYear executionYear = source instanceof SearchStudentsByCurricularCourseParametersBean ? ((SearchStudentsByCurricularCourseParametersBean) source)
		.getExecutionYear()
		: ((ExecutionDegreeListBean) source).getExecutionYear();
		
	if (executionYear != null) {
	    final Person person = AccessControl.getPerson();

	    if (person.isAdministrativeOfficeEmployee()) {
		final Set<Degree> degrees = person.getEmployee().getAdministrativeOffice().getAdministratedDegrees();

		for (DegreeCurricularPlan plan : executionYear.getDegreeCurricularPlans()) {
		    if (degrees.contains(plan.getDegree())) {
			result.add(plan);
		    }
		}
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
