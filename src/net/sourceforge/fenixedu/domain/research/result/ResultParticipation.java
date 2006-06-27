package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ResultParticipation extends ResultParticipation_Base {
    
    public  ResultParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete() {
        removePerson();
        removeResult();
        removeRootDomainObject();
        deleteDomainObject();
    }
}
