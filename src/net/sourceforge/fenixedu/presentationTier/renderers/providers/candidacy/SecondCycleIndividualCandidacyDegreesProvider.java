package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.ChooseDegreeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SecondCycleIndividualCandidacyDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof SecondCycleIndividualCandidacyProcessBean) {
	    final SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) source;
	    return bean.getAvailableDegrees();
	}
	if (source instanceof ChooseDegreeBean) {
	    final ChooseDegreeBean bean = (ChooseDegreeBean) source;
	    final CandidacyProcess candidacyProcess = bean.getCandidacyProcess();
	    if (candidacyProcess instanceof SecondCycleCandidacyProcess) {
		final SecondCycleCandidacyProcess secondCycleCandidacyProcess = (SecondCycleCandidacyProcess) candidacyProcess;
		return secondCycleCandidacyProcess.getAvailableDegrees();
	    }
	}
	return Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
