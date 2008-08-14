package net.sourceforge.fenixedu.dataTransferObject.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;

public class ProjectParticipantFullCreationBean implements Serializable {

    private DomainReference<Unit> organization;
    private String role;
    private String personName;
    private String organizationName;

    public ProjectParticipation.ProjectParticipationType getRole() {
	return ProjectParticipation.ProjectParticipationType.valueOf(role);
    }

    public void setRole(ProjectParticipation.ProjectParticipationType projectParticipationRole) {
	this.role = projectParticipationRole.toString();
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

    public void setOrganization(Unit organization) {
	this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }

}
