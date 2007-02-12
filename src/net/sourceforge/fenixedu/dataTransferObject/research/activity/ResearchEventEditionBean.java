package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;

public class ResearchEventEditionBean extends ResearchActivityEditionBean implements Serializable {
	private DomainReference<Event> event;
    
    public ResearchEventEditionBean() {
    	setEvent(null);
    }
    
    public Event getEvent() {
		return event.getObject();
	}

	public void setEvent(Event event) {
		this.event = new DomainReference<Event>(event);
	}
}
