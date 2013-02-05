package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdIndividualProgramProcessStateProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdIndividualProgramProcessBean processBean = (PhdIndividualProgramProcessBean) source;
        PhdIndividualProgramProcess process = processBean.getIndividualProgramProcess();

        return PhdIndividualProgramProcessState.getPossibleNextStates(process);
    }
}
