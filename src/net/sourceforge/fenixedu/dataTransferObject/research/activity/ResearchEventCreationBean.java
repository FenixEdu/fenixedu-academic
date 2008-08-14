package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ResearchEventCreationBean implements Serializable {
    private DomainReference<ResearchEvent> event;
    private ResearchActivityParticipationRole role;
    private String eventName;
    private EventType eventType;
    private ScopeType locationType;
    private String url;
    private MultiLanguageString roleMessage;

    public MultiLanguageString getRoleMessage() {
	return roleMessage;
    }

    public void setRoleMessage(MultiLanguageString roleMessage) {
	this.roleMessage = roleMessage;
    }

    public ResearchEventCreationBean() {
	setEvent(null);
    }

    public ResearchActivityParticipationRole getRole() {
	return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
	this.role = role;
    }

    public ScopeType getLocationType() {
	return locationType;
    }

    public void setLocationType(ScopeType locationType) {
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

    public ResearchEvent getEvent() {
	return event.getObject();
    }

    public void setEvent(ResearchEvent event) {
	this.event = new DomainReference<ResearchEvent>(event);
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

}
