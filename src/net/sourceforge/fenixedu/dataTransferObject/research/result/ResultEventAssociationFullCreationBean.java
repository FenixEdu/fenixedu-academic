package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ResultEventAssociationFullCreationBean implements Serializable {
    private String role;
    private MultiLanguageString eventName;
    private String eventType;

    public ResultEventAssociationFullCreationBean() {
        eventType = EventType.getDefaultType().toString();
    }
    
    public ResultEventAssociationRole getRole() {
        return ResultEventAssociationRole.valueOf(role);
    }

    public void setRole(ResultEventAssociationRole associationRole) {
        this.role = associationRole.toString();
    }

    public MultiLanguageString getEventName() {
        return eventName;
    }

    public void setEventName(MultiLanguageString name) {
        this.eventName = name;
    }

    public EventType getEventType() {
        return EventType.valueOf(eventType);
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType.toString();
    }    
}
