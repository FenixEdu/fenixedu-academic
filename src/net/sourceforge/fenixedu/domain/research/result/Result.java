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
    
    public void setParticipation(Person author){
        ResultParticipation resultParticipation = new ResultParticipation();
        
        resultParticipation.setPerson(author);
        resultParticipation.setResult(this);
        int order = this.getResultParticipationsCount();
        
        resultParticipation.setPersonOrder(new Integer(order));
    }
 
    public void setParticipations(List<Person> authors) {
    	removeParticipations();
	    for (Person person : authors) {
	        final ResultParticipation resultParticipation = new ResultParticipation();
            
            resultParticipation.setPerson(person);
            resultParticipation.setResult(this);
            resultParticipation.setPersonOrder(new Integer(authors.indexOf(person)));
	    }
	}
    
    public List<Person> getParticipations(){
        List<Person> personsList = new ArrayList<Person>();
        
        for (ResultParticipation resultParticipation : getResultParticipations()) {
            personsList.add(resultParticipation.getPerson());
        }
        return personsList;
    }
    
	public void removeParticipations() {
	    for (Iterator<ResultParticipation> iterator = getResultParticipationsIterator(); iterator.hasNext(); ) {
            ResultParticipation resultParticipation = iterator.next();
	        iterator.remove();
            resultParticipation.delete();
	    }
	}
}
