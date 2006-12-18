package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication.ScopeType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public abstract class ConferenceArticlesBean extends ResultPublicationBean implements Serializable {
    private ScopeType scope;

    private String eventNameAutoComplete;

    private String conference;
    
    private MultiLanguageString eventName;

    // default type is Conference
    private EventType eventType = EventType.Conference;

    private String eventLocal;

    private YearMonthDay eventStartDate;

    private YearMonthDay eventEndDate;

    @Override
    public void setCreateEvent(Boolean createEvent) {
	this.setEventName(new MultiLanguageString(this.getEventNameAutoComplete()));
	super.setCreateEvent(createEvent);
    }

    public YearMonthDay getEventEndDate() {
	return eventEndDate;
    }

    public void setEventEndDate(YearMonthDay eventEndDate) {
	this.eventEndDate = eventEndDate;
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

   

    public ScopeType getScope() {
	return scope;
    }

    public void setScope(ScopeType scope) {
	this.scope = scope;
    }

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}
}
