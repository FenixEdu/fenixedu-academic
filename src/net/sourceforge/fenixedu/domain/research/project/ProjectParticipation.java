package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class ProjectParticipation extends ProjectParticipation_Base {
    
//    public  ProjectParticipation(Project project, Party party, ProjectParticipationType role) {
//        super();
//        setProject(project);
//        setParty(party);
//        setRole(role);
//        setRootDomainObject(RootDomainObject.getInstance());
//    }
    
    
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
