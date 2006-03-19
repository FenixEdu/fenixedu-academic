package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ProjectParticipation extends ProjectParticipation_Base {
    
    public  ProjectParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        
    }
    
    public enum ProjectParticipationType {
        Coordinator,
        Speaker,
        Sponsor,
        Participant;
    }

}
