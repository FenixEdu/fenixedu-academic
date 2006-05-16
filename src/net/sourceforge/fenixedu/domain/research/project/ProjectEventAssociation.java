package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ProjectEventAssociation extends ProjectEventAssociation_Base {
    
    public  ProjectEventAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        
    }
    
    public void delete(){
        this.removeEvent();
        this.removeProject();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public enum ProjectEventAssociationRole {
        Sponsor,
        Presentation, 
    }

}