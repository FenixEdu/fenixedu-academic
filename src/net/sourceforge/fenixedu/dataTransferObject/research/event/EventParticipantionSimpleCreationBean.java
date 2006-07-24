package net.sourceforge.fenixedu.dataTransferObject.research.event;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole;

public class EventParticipantionSimpleCreationBean implements Serializable {
  
    private DomainReference<Event> event;
    private EventParticipationRole role;
    private String eventName;

    public EventParticipationRole getRole() {
        return role;
    }

    public void setRole(EventParticipationRole eventParticipationRole) {
        this.role = eventParticipationRole;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
    
}
