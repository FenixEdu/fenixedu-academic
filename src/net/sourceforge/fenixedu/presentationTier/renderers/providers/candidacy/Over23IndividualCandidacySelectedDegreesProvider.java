package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class Over23IndividualCandidacySelectedDegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final Over23IndividualCandidacyResultBean bean = (Over23IndividualCandidacyResultBean) source;
        return bean.getCandidacyProcess().getSelectedDegreesSortedByOrder();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
