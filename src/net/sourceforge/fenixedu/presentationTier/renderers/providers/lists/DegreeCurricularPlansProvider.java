package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final SortedSet<DegreeCurricularPlan> result =
				new TreeSet<DegreeCurricularPlan>(
						DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

		ExecutionDegreeListBean executionDegreeListBean = (ExecutionDegreeListBean) source;
		if (executionDegreeListBean.getDegree() != null) {
			result.addAll(executionDegreeListBean.getDegree().getDegreeCurricularPlans());
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
