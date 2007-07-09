package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public class CreateResearchEvent extends Service {


	public ResearchEvent run(String name, EventType eventType, ScopeType locationType, String url) {
	
		ResearchEvent event = new ResearchEvent(name, eventType, locationType);
		event.setUrl(url);
		
		return event;
	}
}
