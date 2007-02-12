package net.sourceforge.fenixedu.dataTransferObject.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ProjectEventAssociationFullCreationBean implements Serializable {
  
    private String role;
    private MultiLanguageString eventName;
    private String eventType;

    public ProjectEventAssociationFullCreationBean() {
        eventType = EventType.getDefaultType().toString();
    }
    
    public ProjectEventAssociationRole getRole() {
        return ProjectEventAssociationRole.valueOf(role);
    }

    public void setRole(ProjectEventAssociationRole associationRole) {
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
