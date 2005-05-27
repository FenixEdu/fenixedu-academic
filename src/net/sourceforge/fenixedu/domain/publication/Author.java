/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;


import java.util.ArrayList;
/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Author extends Author_Base implements IAuthor{

	public Author(){	
	    super();
	    setAuthorPublications(new ArrayList());
	}
	
	public String toString() {
		return "keyPerson: "+getKeyPerson()+", person: "+getPerson()+", author: "+getAuthor()+", organization: "+getOrganization();
	}
	
	public boolean equals(IAuthor author){
	    if (author != null) {
	        if (getKeyPerson() != null && author.getKeyPerson() != null){
	            return getKeyPerson() == author.getKeyPerson();
	        }
            if (getKeyPerson() == null || author.getKeyPerson() == null){
                return false;
            }
            if (getOrganization() == null && author.getOrganization() == null) {
                return this.getAuthor().equals(author.getAuthor());
            }
            else
                if (getOrganization() == null || author.getOrganization() == null)
                    return false;
                else return ( this.getAuthor().equals(author.getAuthor()) &&
                        getOrganization().equals(author.getOrganization()));
	    }
        return false;
	}

}
