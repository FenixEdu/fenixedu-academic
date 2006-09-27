package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (executionDegreeBean.getDegree() != null) {
	    result.addAll(executionDegreeBean.getDegree().getDegreeCurricularPlansSet());
	    Collections.sort(result, new BeanComparator("name"));
	} else {
	    executionDegreeBean.setDegreeCurricularPlan(null);
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
