package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.event.Event;

public class ResultEventAssociation extends ResultEventAssociation_Base {
    
    public  ResultEventAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete(){
        final Event event = this.getEvent();
        final Result result = this.getResult();        
        this.removeEvent();
        event.sweep();
        
        this.removeResult();
        result.sweep();
        
        this.removeRootDomainObject();
        deleteDomainObject();
    }
   
    public enum ResultEventAssociationRole {
        Exhibitor,
        Participant;
        
        public String getName() {
            return name();
        }
    }    
}
