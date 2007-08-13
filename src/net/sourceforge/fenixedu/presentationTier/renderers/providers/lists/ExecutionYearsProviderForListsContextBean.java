package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionYearsProviderForListsContextBean implements DataProvider {

    
    public Object provide(Object source, Object currentValue) {
	final SortedSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);

	final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
	if (executionDegreeBean.getDegreeCurricularPlan() != null) {
	    final SortedSet<ExecutionDegree> executionDegrees = new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	    executionDegrees.addAll(executionDegreeBean.getDegreeCurricularPlan().getExecutionDegrees());
	    
	    for (ExecutionDegree exeDegree : executionDegrees) {
		executionYears.add(exeDegree.getExecutionYear());
	    }
	} else if (executionDegreeBean.getDegreeCurricularPlan() == null) {
	    executionYears.addAll(ExecutionYear.readNotClosedExecutionYears());
	}

	return executionYears;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
