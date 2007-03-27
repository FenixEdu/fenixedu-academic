package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class EventParticipantBean extends ParticipantBean implements Serializable {

    DomainReference<Event> event;
    
    public EventParticipantBean() {
	super();
	this.setEvent(null);
    }
    
    public Event getEvent() {
	return this.event.getObject();
    }
    
    public void setEvent(Event event) {
	this.event = new DomainReference<Event>(event);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    @Override
    public DomainObject getActivity() {
	return getEvent();
    }
} 
