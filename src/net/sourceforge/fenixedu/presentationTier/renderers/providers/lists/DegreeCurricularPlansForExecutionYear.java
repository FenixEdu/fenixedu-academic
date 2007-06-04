package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeCurricularPlansForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<DegreeCurricularPlan> result = new TreeSet<DegreeCurricularPlan>(DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
	if (executionDegreeBean.getExecutionYear() != null) {
	    final Person person = AccessControl.getPerson();

	    final Set<Degree> degrees;
	    if (person.isAdministrativeOfficeEmployee()) {
		degrees = person.getEmployee().getAdministrativeOffice().getAdministratedDegrees();
	    } else {
		degrees = RootDomainObject.getInstance().getDegreesSet();
	    }

	    for (DegreeCurricularPlan plan : executionDegreeBean.getExecutionYear().getDegreeCurricularPlans()) {
		if (degrees.contains(plan.getDegree())) {
		    result.add(plan);
		}
	    }
	} else {
	    executionDegreeBean.setDegreeCurricularPlan(null);
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
