package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import org.joda.time.YearMonthDay;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ConferenceArticlesBean extends ResultPublicationBean implements Serializable{
    private DomainReference<Event> event;
    private String eventNameAutoComplete;
    
    //event creation needed to create a event
    private MultiLanguageString eventName;
    private EventType eventType;
    private String eventLocal;
    private YearMonthDay eventStartDate;
    private YearMonthDay eventEndDate;
    private Boolean eventFee;
    

	public YearMonthDay getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(YearMonthDay eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public Boolean getEventFee() {
		return eventFee;
	}
	public void setEventFee(Boolean eventFee) {
		this.eventFee = eventFee;
	}
	public String getEventLocal() {
		return eventLocal;
	}
	public void setEventLocal(String eventLocal) {
		this.eventLocal = eventLocal;
	}
	public MultiLanguageString getEventName() {
		return eventName;
	}
	public void setEventName(MultiLanguageString eventName) {
		this.eventName = eventName;
	}
	public YearMonthDay getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(YearMonthDay eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public String getEventNameAutoComplete() {
        return eventNameAutoComplete;
    }
    public void setEventNameAutoComplete(String name) {
        this.eventNameAutoComplete = name;
    }
    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }
    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
}
