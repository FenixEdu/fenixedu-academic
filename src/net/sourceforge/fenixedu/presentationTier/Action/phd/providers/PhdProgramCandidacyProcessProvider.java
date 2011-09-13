package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdProgramCandidacyProcessProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new EnumConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	return PhdProgramCandidacyProcessState.getPossibleNextStates(((PhdProgramCandidacyProcessBean) source).getProcess());
    }

}
