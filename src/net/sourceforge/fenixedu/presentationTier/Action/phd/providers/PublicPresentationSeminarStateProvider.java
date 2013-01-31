package net.sourceforge.fenixedu.presentationTier.Action.phd.providers;

import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PublicPresentationSeminarStateProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) source;
		return bean.getProcess().getPossibleNextStates();
	}

}
