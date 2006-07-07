package net.sourceforge.fenixedu.dataTransferObject.research.event;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole;

public class EventParticipantFullCreationBean implements Serializable {
  
    private DomainReference<Unit> organization;
    private EventParticipationRole role;
    private String personName;
    private String organizationName;

    public EventParticipationRole getRole() {
        return role;
    }

    public void setRole(EventParticipationRole role) {
        this.role = role;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String name) {
        this.personName = name;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String name) {
        this.organizationName = name;
    }    
    
    public Unit getOrganization() {
        return (this.organization == null) ? null : this.organization.getObject();
    }

    public void setOrganization (Unit organization) {
        this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }

}
