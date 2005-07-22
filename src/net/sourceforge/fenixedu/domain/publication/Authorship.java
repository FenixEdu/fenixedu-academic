package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Authorship extends Authorship_Base {
    
    public Authorship() {
        super();
    }
   
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/

    
    public Authorship(IPublication publication, IPerson person, Integer order) throws DomainException {
    	//Check to see if the publication allready has an authorship with that order
    	for (IAuthorship authorship : publication.getPublicationAuthorships()) {
    		if (authorship.getOrder() == order) 
    			throw new DomainException("errors.publications.authorshipWithIncorrectOrder", publication.getTitle(), order.toString());
		}
        setPublication(publication);
        setAuthor(person);
        setOrder(order);
    }


    public void delete()
    {
        removeAuthor();
        removePublication();
        super.deleteDomainObject();
    }
}
