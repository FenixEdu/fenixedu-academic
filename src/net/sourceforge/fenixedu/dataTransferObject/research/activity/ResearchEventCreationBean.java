package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class ResearchEventCreationBean implements Serializable {
	private DomainReference<Event> event;
    private ResearchActivityParticipationRole role;
    private String eventName;
    private EventType eventType;
    private ResearchActivityLocationType locationType;
    private String url;

    public ResearchEventCreationBean() {
    	setEvent(null);
    }
    
    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }
    
    public ResearchActivityLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(ResearchActivityLocationType locationType) {
        this.locationType = locationType;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Event getEvent() {
		return event.getObject();
	}

	public void setEvent(Event event) {
		this.event = new DomainReference<Event>(event);
	}
	
	public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
}
