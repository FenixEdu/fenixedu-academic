package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class Over23IndividualCandidacySelectedDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Over23IndividualCandidacyResultBean bean = (Over23IndividualCandidacyResultBean) source;
	return bean.getCandidacyProcess().getSelectedDegreesSortedByOrder();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
