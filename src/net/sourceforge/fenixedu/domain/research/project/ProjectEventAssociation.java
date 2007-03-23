package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;

public class ProjectEventAssociation extends ProjectEventAssociation_Base {
    
    public  ProjectEventAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        
    }
   
    public void delete(){
        final EventEdition event = this.getEventEdition();
        final Project project = this.getProject();
        
        this.removeEventEdition();
        event.sweep();
        
        this.removeProject();
        project.sweep();
        
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public enum ProjectEventAssociationRole {
        Exhibitor,
        Participant, 
    }

}