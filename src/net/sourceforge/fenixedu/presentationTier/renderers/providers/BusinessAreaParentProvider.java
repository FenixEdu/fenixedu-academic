package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.BusinessArea;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BusinessAreaParentProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return BusinessArea.getParentBusinessAreas();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
