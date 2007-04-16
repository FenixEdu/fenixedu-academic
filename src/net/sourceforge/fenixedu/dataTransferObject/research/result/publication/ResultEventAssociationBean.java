package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

import org.joda.time.YearMonthDay;

public class ResultEventAssociationBean implements Serializable {
	private DomainReference<Event> event;
	private DomainReference<EventEdition> eventEdition;
    private String eventName;
    private EventType eventType;
    private ScopeType locationType;
    private String eventLocation;
    private YearMonthDay startDate;
    private YearMonthDay endDate;
    private String organization;
    private String edition;
    //private String schema;
    private String eventUrl;
    private String eventEditionUrl;
    
    private boolean eventAlreadyChosen;
    private boolean createEventEdition;
    
    

    public ResultEventAssociationBean() {
    	setEvent(null);
    	setEventEdition(null);
    	eventAlreadyChosen = false;
    	createEventEdition = false;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    
    public ScopeType getLocationType() {
        return locationType;
    }

    public void setLocationType(ScopeType locationType) {
        this.locationType = locationType;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
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
	
	public EventEdition getEventEdition() {
		return (eventEdition != null) ? eventEdition.getObject() : null;
	}

	public void setEventEdition(EventEdition eventEdition) {
		this.eventEdition = (eventEdition != null) ? new DomainReference<EventEdition>(eventEdition) : null;
	}
	
	public String getEdition() {
		return edition;
	}
	
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public String getEventUrl() {
		return eventUrl;
	}
	
	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}
	
	public String getEventEditionUrl() {
		return eventEditionUrl;
	}
	
	public void setEventEditionUrl(String eventEditionUrl) {
		this.eventEditionUrl = eventEditionUrl;
	}
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getOrganization() {
		return organization;
	}
	
	public boolean getEventAlreadyChosen() {
	    return eventEdition!=null || edition != null;
	}
		
	public void setCreateNewEventEdition(boolean createEventEdition) {
		this.createEventEdition = createEventEdition;
	}
	
	public boolean getCreateNewEventEdition() {
		return createEventEdition;
	}
	
	public void setNextStepBeanState(){
		if (!this.eventAlreadyChosen && this.getEvent() != null){
			this.eventAlreadyChosen = true;
		}
	}
	
	public boolean getSelectEventState(){
		return (this.getEvent() == null && !this.eventAlreadyChosen);
	}
	
	public boolean getSelectEventEditionState(){
		return (this.getEvent() != null && this.eventAlreadyChosen && !this.createEventEdition);
	}
	
	public void setNewEventEditionBeanState(){
		this.edition = (this.getEvent() != null ? this.getEvent().getName() : this.eventName);
		this.setEventEdition(null);
		this.createEventEdition = true;
	}
	
	public boolean getNewEventEditionState(){
		return (this.getEvent() != null && this.createEventEdition);
	}
	
	public void setNewEventBeanState(){
		this.edition = (this.getEvent() != null ? this.getEvent().getName() : this.eventName);
		this.eventAlreadyChosen = true;
		this.createEventEdition = true;
	}
	
	public boolean getNewEventState(){
		return (this.getEvent() == null && this.eventAlreadyChosen && this.createEventEdition);
	}
}
