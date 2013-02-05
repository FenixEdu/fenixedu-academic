package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalsFilterBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ProposalStatusFilterProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;

    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return ((ProposalsFilterBean) source).getStatusCount();
    }

}
