package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionYearsProviderForListsContextBean implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
		List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();

		if (executionDegreeBean.getDegreeCurricularPlan() != null) {

			final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(
					executionDegreeBean.getDegreeCurricularPlan()
							.getExecutionDegrees());
			Collections
					.sort(
							executionDegrees,
							ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
			for (ExecutionDegree exeDegree : executionDegrees) {
				executionYears.add(exeDegree.getExecutionYear());
			}
		} else if (executionDegreeBean.getDegree() == null) {
			executionYears = new ArrayList<ExecutionYear>(ExecutionYear.readNotClosedExecutionYears());
			Collections.reverse(executionYears);
					
		}
		Collections.sort(executionYears,
				ExecutionYear.EXECUTION_YEAR_COMPARATOR_BY_YEAR);
		
		return executionYears;
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
