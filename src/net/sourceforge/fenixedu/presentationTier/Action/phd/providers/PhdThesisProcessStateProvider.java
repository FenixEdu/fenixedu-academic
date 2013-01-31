package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdThesisProcessStateProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		PhdThesisProcessBean bean = (PhdThesisProcessBean) source;
		return bean.getThesisProcess().getPossibleNextStates();
	}

}
