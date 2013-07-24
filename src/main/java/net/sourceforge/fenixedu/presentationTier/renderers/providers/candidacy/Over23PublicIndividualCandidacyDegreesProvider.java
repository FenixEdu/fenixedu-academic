package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class Over23PublicIndividualCandidacyDegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Over23IndividualCandidacyProcessBean over23IndividualCandidacyProcessBean = (Over23IndividualCandidacyProcessBean) source;
        if (over23IndividualCandidacyProcessBean.getCandidacyProcess() != null) {
            return over23IndividualCandidacyProcessBean.getCandidacyProcess().getDegree();
        } else {
            return over23IndividualCandidacyProcessBean.getIndividualCandidacyProcess().getCandidacyProcess().getDegree();
        }
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
