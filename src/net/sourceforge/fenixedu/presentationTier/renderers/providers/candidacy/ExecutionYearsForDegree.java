package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionYearsForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(
		new ReverseComparator(ExecutionYear.COMPARATOR_BY_YEAR));

	final DFACandidacyBean bean = (DFACandidacyBean) source;
	if (bean.getDegree() != null) {
	    for (final DegreeCurricularPlan dcp : bean.getDegree().getDegreeCurricularPlansSet()) {
		result.addAll(dcp.getExecutionYears());
	    }
	} else {
	    bean.setExecutionYear(null);
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
