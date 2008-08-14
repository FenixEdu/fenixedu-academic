package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchObjectsByMultiLanguageString;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

public class SearchEventsByType extends SearchObjectsByMultiLanguageString {

    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
	if (type != ResearchEvent.class)
	    return null;

	final Collection objects = super.run(type, value, limit, arguments);
	List<DomainObject> result = new ArrayList<DomainObject>();

	EventType eventType;
	try {
	    String eventTypeString = arguments.get("type");
	    eventType = EventType.valueOf(eventTypeString);
	} catch (Exception e) {
	    return null;
	}

	for (Object object : objects) {
	    ResearchEvent event = (ResearchEvent) object;
	    if (event.getEventType() == eventType)
		result.add(event);
	}
	return result;
    }
}
