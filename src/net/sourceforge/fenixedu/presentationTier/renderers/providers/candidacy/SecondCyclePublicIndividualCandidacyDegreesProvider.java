package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SecondCyclePublicIndividualCandidacyDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) source;
	final SecondCycleCandidacyProcess candidacyProcess = bean.getCandidacyProcess();
	return candidacyProcess.getAvailableDegrees();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
