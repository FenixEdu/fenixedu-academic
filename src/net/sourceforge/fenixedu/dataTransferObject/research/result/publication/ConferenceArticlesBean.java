package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import org.joda.time.YearMonthDay;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ScopeType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public abstract class ConferenceArticlesBean extends ResultPublicationBean implements Serializable {
    private ScopeType scope;

    private DomainReference<Event> event;

    private String eventNameAutoComplete;

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

    public Event getEvent() {
	return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(Event event) {
	this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public ScopeType getScope() {
	return scope;
    }

    public void setScope(ScopeType scope) {
	this.scope = scope;
    }
}
