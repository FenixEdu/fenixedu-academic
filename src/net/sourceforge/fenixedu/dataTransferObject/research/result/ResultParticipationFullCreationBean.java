package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class ResultParticipationFullCreationBean implements Serializable{
    private DomainReference<Unit> organization;
    private String personName;
    private String organizationName;
    private ResultParticipationRole resultParticipationRole;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String name) {
        this.personName = name;
    }
    
    public ResultParticipationRole getResultParticipationRole() {
        return resultParticipationRole;
    }

    public void setResultParticipationRole(ResultParticipationRole resultParticipationRole) {
        this.resultParticipationRole = resultParticipationRole;
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
