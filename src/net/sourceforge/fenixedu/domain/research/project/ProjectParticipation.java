package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ProjectParticipation extends ProjectParticipation_Base {
    
    public  ProjectParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        
    }
    
    public void delete(){
        this.removeParty();
        this.removeProject();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public enum ProjectParticipationType {
        Coordinator,
        Speaker,
        Sponsor,
        Participant;
    }

}
