 package net.sourceforge.fenixedu.domain.research.result;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.Partial;

public class Patent extends Patent_Base {
    
    public  Patent() {
        super();
    }
 
    public void delete() {
        
        for (Iterator<Authorship> iterator = getResultAuthorshipsIterator(); iterator.hasNext(); ) {
            Authorship authorship = iterator.next();
            iterator.remove();
            authorship.delete();
        }
    	removeRootDomainObject();
        super.deleteDomainObject();
    }
    
}
