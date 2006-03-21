package net.sourceforge.fenixedu.domain.research.result;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;


public class Result extends Result_Base {
    
    public  Result() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
 
    protected void setAuthorships(List<Person> authors) {
    	removeAuthorships();
	    int i = 1;
	    for (final Iterator iterator = authors.iterator(); iterator.hasNext(); i++) {
	        final Person author = (Person) iterator.next();
	        final Authorship authorship = new Authorship();
	
	        authorship.setAuthor(author);
	        authorship.setResult(this);
	        authorship.setOrder(new Integer(i));
	    }
	}
	
	protected void removeAuthorships() {
	    for (Iterator<Authorship> iterator = getResultAuthorshipsIterator(); iterator.hasNext(); ) {
	        Authorship authorship = iterator.next();
	        iterator.remove();
	        authorship.delete();
	    }
	    
	}
}
