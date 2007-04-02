package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchEventEditionCreationBean implements Serializable {

    DomainReference<Event> event;
    DomainReference<EventEdition> edition;
    ResearchActivityParticipationRole role;
    String eventEditionName;
    String eventName;
    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Event getEvent() {
	return this.event.getObject();
    }
    
    public void setEvent(Event event) {
	this.event = new DomainReference<Event>(event);
    }
    
    public String getEventEditionName() {
        return eventEditionName;
    }

    public void setEventEditionName(String eventEditionName) {
        this.eventEditionName = eventEditionName;
    }

    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }

    public ResearchEventEditionCreationBean() {
	super();
	this.edition = new DomainReference<EventEdition>(null);
	this.event = new DomainReference<Event>(null);
    }
    
    public EventEdition getEventEdition() {
	return this.edition.getObject();
    }
    
    public void setEventEdition(EventEdition edition) {
	this.edition = new DomainReference<EventEdition>(edition);
    }
    
}
