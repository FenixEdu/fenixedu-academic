package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeChangePublicIndividualCandidacyDegreesProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
				(IndividualCandidacyProcessWithPrecedentDegreeInformationBean) source;

		if (bean.getCandidacyProcess() != null) {
			return bean.getCandidacyProcess().getDegree();
		} else {
			return bean.getIndividualCandidacyProcess().getCandidacyProcess().getDegree();
		}
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
