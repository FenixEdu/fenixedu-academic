package net.sourceforge.fenixedu.dataTransferObject.research.event;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole;

public class EventParticipantUnitCreationBean implements Serializable {
  
    private DomainReference<Unit> unit;
    private EventParticipationRole role;
    private String unitName;

    public EventParticipantUnitCreationBean() {
        role = EventParticipationRole.getDefaultUnitRoleType();
    }
    
    public EventParticipationRole getRole() {
        return role; 
    }

    public void setRole(EventParticipationRole role) {
        this.role = role;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String name) {
        this.unitName = name;
    }

    public Unit getUnit() {
        return (this.unit == null) ? null : this.unit.getObject();
    }

    public void setUnit(Unit unit) {
        this.unit = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }

}
