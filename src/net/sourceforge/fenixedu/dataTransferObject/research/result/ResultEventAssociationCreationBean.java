package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;

public class ResultEventAssociationCreationBean implements Serializable {
    private DomainReference<ResearchResult> result;
    private DomainReference<EventEdition> event;
    private ResultEventAssociationRole role;
    private String eventNameMLS;
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
    
    public String getEventNameMLS() {
        return eventNameMLS;
    }

    public void setEventNameMLS(String name) {
        this.eventNameMLS = name;
    }

    public EventEdition getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(EventEdition event) {
        this.event = (event != null) ? new DomainReference<EventEdition>(event) : null;
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
