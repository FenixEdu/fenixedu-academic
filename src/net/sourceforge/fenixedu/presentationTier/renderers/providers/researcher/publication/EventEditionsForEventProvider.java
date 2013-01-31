package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.publication;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EventEditionsForEventProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		ResearchEvent event;
		try {
			event = (ResearchEvent) MethodUtils.invokeMethod(source, "getEvent", null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return event == null ? Collections.EMPTY_LIST : event.getEventEditions();
	}

}
