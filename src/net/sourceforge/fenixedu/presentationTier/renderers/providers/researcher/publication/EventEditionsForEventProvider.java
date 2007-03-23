package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.publication;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class EventEditionsForEventProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	Event event;
	try {
		event = (Event) MethodUtils.invokeMethod(source, "getEvent", null);
	}catch(Exception e) {
		throw new RuntimeException(e);
	}
	
	return event==null ? Collections.EMPTY_LIST : event.getEventEditions();
    }

}
