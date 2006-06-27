 package net.sourceforge.fenixedu.domain.research.result;

import java.util.Iterator;

public class ResultPatent extends ResultPatent_Base {
    
    public  ResultPatent() {
        super();
    }
 
    public void delete() {
        
        for (Iterator<ResultParticipation> iterator = getResultParticipationsIterator(); iterator.hasNext(); ) {
            ResultParticipation resultParticipation = iterator.next();
            iterator.remove();
            resultParticipation.delete();
        }
    	removeRootDomainObject();
        super.deleteDomainObject();
    }
    
}
