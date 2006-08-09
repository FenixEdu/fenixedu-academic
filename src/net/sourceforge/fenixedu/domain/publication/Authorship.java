package net.sourceforge.fenixedu.domain.publication;

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

    
    public Authorship(Publication publication, Person person, Integer order) throws DomainException {
    	//Check to see if the publication allready has an authorship with that order
    	for (Authorship authorship : publication.getPublicationAuthorships()) {
    		if (authorship.getOrder() == order) 
    			throw new DomainException("errors.publications.authorshipWithIncorrectOrder", publication.getTitle(), order.toString());
		}
        setPublication(publication);
        setAuthor(person);
        setOrder(order);
    }

    public void delete() {
    	removeAuthor();
        removePublication();
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

