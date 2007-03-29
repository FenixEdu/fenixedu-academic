package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class CreateResearchEvent extends Service {


	public Event run(String name, EventType eventType, ScopeType locationType, String url) {
	
		Event event = new Event(name, eventType, locationType);
		event.setUrl(url);
		
		return event;
	}
}
