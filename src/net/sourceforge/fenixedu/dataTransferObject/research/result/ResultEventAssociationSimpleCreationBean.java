package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;

public class ResultEventAssociationSimpleCreationBean implements Serializable {
    
    private DomainReference<Event> event;
    private String role;
    private String eventName;

    public ResultEventAssociationSimpleCreationBean() {
        role = ResultEventAssociationRole.getDefaultEventRoleType().toString();
    }
    
    public ResultEventAssociationRole getRole() {
        return ResultEventAssociationRole.valueOf(role);
    }

    public void setRole(ResultEventAssociationRole associationRole) {
        this.role = associationRole.toString();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
}
