package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
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
 
    public void setAuthorships(List<Person> authors) {
    	removeAuthorships();
	    for (Person person : authors) {
	        final Authorship authorship = new Authorship();
            
	        authorship.setAuthor(person);
	        authorship.setResult(this);
	        authorship.setOrder(new Integer(authors.indexOf(person)));
	    }
	}
    
    public List<Person> getAuthorships(){
        List<Person> authorsList = new ArrayList<Person>();
        
        for (Authorship author : getResultAuthorships()) {
            authorsList.add(author.getAuthor());
        }
        return authorsList;
    }
    
	public void removeAuthorships() {
	    for (Iterator<Authorship> iterator = getResultAuthorshipsIterator(); iterator.hasNext(); ) {
	        Authorship authorship = iterator.next();
	        iterator.remove();
            //((Authorship) rootDomainObject.readAuthorshipByOID(authorship.getIdInternal())).delete();
	        authorship.delete();
	    }
	}
}
