package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ManageThesisContext;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ManageThesisContextProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final ManageThesisContext manageThesisContext = (ManageThesisContext) source;
		return manageThesisContext.getAvailableExecutionDegrees();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
