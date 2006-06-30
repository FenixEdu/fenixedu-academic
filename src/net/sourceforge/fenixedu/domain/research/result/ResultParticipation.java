package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ResultParticipation extends ResultParticipation_Base {
    
    public  ResultParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    /** 
     * This method is responsible for deleting the object.
     */
    public void delete() {
        final Result result = this.getResult(); 
            
        this.removePerson();
        this.removeResult();
        
        result.sweep();
        
        this.removeRootDomainObject();
        deleteDomainObject();
    }
}
