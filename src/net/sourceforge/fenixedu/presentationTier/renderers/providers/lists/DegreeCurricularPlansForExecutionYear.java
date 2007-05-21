package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeCurricularPlansForExecutionYear implements DataProvider {

	public Object provide(Object source, Object currentValue) {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
		final List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();

	
		if (executionDegreeBean.getExecutionYear() != null) {
			final Set<Degree> degrees = AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegrees();
			
			for (DegreeCurricularPlan plan : executionDegreeBean.getExecutionYear()
					.getDegreeCurricularPlans()) {
				if (degrees.contains(plan.getDegree())){
					degreeCurricularPlans.add(plan);
				}
			}
			
			Collections.sort(degreeCurricularPlans,DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
		} else {
			executionDegreeBean.setDegreeCurricularPlan(null);
		}
		
		return degreeCurricularPlans;
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
