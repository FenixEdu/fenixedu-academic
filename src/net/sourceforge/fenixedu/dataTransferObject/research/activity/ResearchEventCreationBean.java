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
    private String userInput;
    private MultiLanguageString eventName;
    private EventType eventType;
    private ResearchActivityLocationType locationType;
    private String eventLocation;
    private YearMonthDay startDate;
    private YearMonthDay endDate;
    

    public ResearchEventCreationBean() {
    	setEvent(null);
    }
    
    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
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
    
    public ResearchActivityLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(ResearchActivityLocationType locationType) {
        this.locationType = locationType;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }
    
    public MultiLanguageString getEventName() {
        return eventName;
    }
    
    public void setEventName(MultiLanguageString eventName) {
        this.eventName = eventName;
    }
    
    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
    	this.userInput = userInput;
        this.eventName = new MultiLanguageString(this.userInput);
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
    
}
