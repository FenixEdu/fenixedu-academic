package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (dfaCandidacyBean.getDegree() != null && dfaCandidacyBean.getExecutionYear() != null) {
	    result.addAll(dfaCandidacyBean.getDegree().getDegreeCurricularPlansForYear(
		    dfaCandidacyBean.getExecutionYear()));
	    Collections.sort(result, new BeanComparator("name"));
	} else {
	    dfaCandidacyBean.setDegreeCurricularPlan(null);
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
