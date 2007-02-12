package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.project.ProjectType;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class EventProjectAssociationFullCreationBean implements Serializable {
  
    private ProjectEventAssociationRole role;
    private MultiLanguageString projectTitle;
    private ProjectType projectType;

    public EventProjectAssociationFullCreationBean() {
        projectType = ProjectType.getDefaultType();
    }
    
    public ProjectEventAssociationRole getRole() {
        return role;
    }

    public void setRole(ProjectEventAssociationRole associationRole) {
        this.role = associationRole;
    }

    public MultiLanguageString getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(MultiLanguageString title) {
        this.projectTitle = title;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }    
}
