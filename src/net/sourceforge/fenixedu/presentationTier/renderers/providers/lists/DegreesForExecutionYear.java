package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreesForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

	final Set<Degree> administratedDegrees = AccessControl.getPerson().getEmployee().getAdministrativeOffice()
		.getAdministratedDegrees();

	final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
	final ExecutionYear executionYear = executionDegreeBean.getExecutionYear();

	if (executionYear != null) {
	    for (ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
		Degree degree = executionDegree.getDegree();
		if (administratedDegrees.contains(degree)) {
		    result.add(degree);
		}
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
