package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Authorship extends Authorship_Base {
    
    public Authorship() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
   
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/

    
    public Authorship(Result result, Person person, Integer order) throws DomainException {
    	//Check to see if the publication allready has an authorship with that order
    	for (Authorship authorship : result.getResultAuthorships()) {
    		if (authorship.getOrder() == order) 
    			throw new DomainException("errors.publications.authorshipWithIncorrectOrder", result.getTitle().getContent(), order.toString());
		}
        setResult(result);
        setAuthor(person);
        setOrder(order);
    }

    public void delete() {
    	removeAuthor();
        removeResult();
        removeRootDomainObject();
        deleteDomainObject();
    }

    @Deprecated
    public Integer getOrder() {
        return super.getAuthorOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setAuthorOrder(order);
    }
    
    
}

