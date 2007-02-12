package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole;

public class EventProjectAssociationSimpleCreationBean implements Serializable {
  
    private DomainReference<Project> project;
    private ProjectEventAssociationRole role;
    private String projectTitle;

    public ProjectEventAssociationRole getRole() {
        return role;
    }

    public void setRole(ProjectEventAssociationRole associationRole) {
        this.role = associationRole;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String title) {
        this.projectTitle = title;
    }

    public Project getProject() {
        return (this.project == null) ? null : this.project.getObject();
    }

    public void setProject(Project project) {
        this.project = (project != null) ? new DomainReference<Project>(project) : null;
    }

}
