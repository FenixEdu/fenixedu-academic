package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PrecedentStudentCurricularPlansProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
				(IndividualCandidacyProcessWithPrecedentDegreeInformationBean) source;
		return bean.getPrecedentStudentCurricularPlans();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
