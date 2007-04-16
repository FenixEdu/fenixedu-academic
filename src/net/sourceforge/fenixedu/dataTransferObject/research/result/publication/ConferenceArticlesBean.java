package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

public abstract class ConferenceArticlesBean extends ResultPublicationBean implements Serializable {
    private ScopeType scope;

    //private String eventNameAutoComplete;

//    private String conference;
    
    private String eventName;

    // default type is Conference
    private EventType eventType = EventType.Conference;

//    private String eventLocal;
//
//    private YearMonthDay eventStartDate;
//
//    private YearMonthDay eventEndDate;
    
    private DomainReference<EventEdition> eventEdition;
    
    private DomainReference<Event> event;

    @Override
    public void setCreateEvent(Boolean createEvent) {
	//this.setEventName(this.getEventNameAutoComplete());
	super.setCreateEvent(createEvent);
    }

//    public YearMonthDay getEventEndDate() {
//	return eventEndDate;
//    }
//
//    public void setEventEndDate(YearMonthDay eventEndDate) {
//	this.eventEndDate = eventEndDate;
//    }
//
//    public String getEventLocal() {
//	return eventLocal;
//    }
//
//    public void setEventLocal(String eventLocal) {
//	this.eventLocal = eventLocal;
//    }

    public String getEventName() {
	return eventName;
    }

    public void setEventName(String eventName) {
	this.eventName = eventName;
    }

//    public YearMonthDay getEventStartDate() {
//	return eventStartDate;
//    }
//
//    public void setEventStartDate(YearMonthDay eventStartDate) {
//	this.eventStartDate = eventStartDate;
//    }

    public EventType getEventType() {
	return eventType;
    }

    public void setEventType(EventType eventType) {
	this.eventType = eventType;
    }

//    public String getEventNameAutoComplete() {
//	return eventNameAutoComplete;
//    }

//    public void setEventNameAutoComplete(String name) {
//	this.eventNameAutoComplete = name;
//    }

    public ScopeType getScope() {
	return scope;
    }

    public void setScope(ScopeType scope) {
	this.scope = scope;
    }
	
	public EventEdition getEventEdition() {
		return (eventEdition != null) ? eventEdition.getObject() : null;
	}

	public void setEventEdition(EventEdition eventEdition) {
		this.eventEdition = (eventEdition != null) ? new DomainReference<EventEdition>(eventEdition) : null;
	}
	
	public Event getEvent() {
		return (event != null) ? event.getObject() : null;
	}

	public void setEvent(Event event) {
		this.event = (event != null) ? new DomainReference<Event>(event) : null;
	}
}
