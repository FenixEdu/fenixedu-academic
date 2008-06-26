package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PrecedentStudentCurricularPlansProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean = (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) source;
	return bean.getPrecedentStudentCurricularPlans();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
