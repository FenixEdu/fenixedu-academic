package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class ExecutionDegreesForDegreeCurricularPlan implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        final CreateDFACandidacyBean createDFACandidacyBean = (CreateDFACandidacyBean) source;
        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        if (createDFACandidacyBean.getDegree() != null && createDFACandidacyBean.getDegreeCurricularPlan() != null) {
            result.addAll(createDFACandidacyBean.getDegreeCurricularPlan().getExecutionDegreesSet());
        }
        Collections.sort(result, new BeanComparator("year"));
        return result;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
