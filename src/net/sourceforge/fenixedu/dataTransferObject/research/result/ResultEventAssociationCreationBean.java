package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ResultEventAssociationCreationBean implements Serializable {
    private DomainReference<ResearchResult> result;
    private DomainReference<Event> event;
    private ResultEventAssociationRole role;
    private MultiLanguageString eventNameMLS;
    private String eventNameStr;
    private EventType eventType;

    public ResultEventAssociationCreationBean(ResearchResult result) {
	setResult(new DomainReference<ResearchResult>(result));
        setRole(ResultEventAssociationRole.getDefaultRole());
        setEvent(null);
        setEventType(EventType.getDefaultType());
        setEventNameMLS(null);
        setEventNameStr(null);
    }
    
    public ResultEventAssociationRole getRole() {
        return this.role;
    }

    public void setRole(ResultEventAssociationRole associationRole) {
        this.role = associationRole;
    }
    
    public MultiLanguageString getEventNameMLS() {
        return eventNameMLS;
    }

    public void setEventNameMLS(MultiLanguageString name) {
        this.eventNameMLS = name;
    }

    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
    
    public ResearchResult getResult() {
        return result.getObject();
    }

    public void setResult(DomainReference<ResearchResult> result) {
        this.result = result;
    }
    
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventNameStr() {
        return eventNameStr;
    }

    public void setEventNameStr(String eventNameStr) {
        this.eventNameStr = eventNameStr;
    }
}
