package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil.elections;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.NewRoundElectionBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class NewRoundElectionsCandidatesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	NewRoundElectionBean bean = (NewRoundElectionBean) source;
	return bean.getCandidates();
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
