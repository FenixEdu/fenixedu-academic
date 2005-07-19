package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.IPerson;

public class Authorship extends Authorship_Base {
    
    public Authorship() {
        super();
    }
   
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public Authorship(IPublication publication, IPerson person, Integer order) {
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
