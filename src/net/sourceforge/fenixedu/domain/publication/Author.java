/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Author extends DomainObject implements IAuthor{

	private Integer keyPerson;
	public IPerson person;
	public String author;
	public String organization;
	
	public List authorPublications;
	
	public Author(){	
	    super();
	    authorPublications = new ArrayList();
	}
	
	
	/**
     * @return Returns the author.
     */
	public String getAuthor() {
		return author;
	}

	/**
     * @return Returns the keyPerson.
     */
	public Integer getKeyPerson() {
		return keyPerson;
	}

	/**
     * @return Returns the organization.
     */
	public String getOrganization() {
		return organization;
	}

	/**
     * @return Returns the pessoa.
     */
	public IPerson getPerson() {
		return person;
	}

	/**
     * @param author
     *            The author to set.
     */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
     * @param keyPerson
     *            The keyPerson to set.
     */
	public void setKeyPerson(Integer keyPerson) {
		this.keyPerson = keyPerson;
	}

	/**
     * @param organization
     *            The organization to set.
     */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
     * @param pessoa
     *            The pessoa to set.
     */
	public void setPerson(IPerson person) {
		this.person = person;
	}

	/**
	 * @return Returns the publicationAuthors.
	 */
	public List getAuthorPublications() {
		return authorPublications;
	}

	/**
	 * @param publicationAuthors The publicationAuthors to set.
	 */
	public void setAuthorPublications(List authorPublications) {
		this.authorPublications = authorPublications;
	}
	
	public List getPublications(){
	    List publications = (List) CollectionUtils.collect(authorPublications, new Transformer() {
	       public Object transform(Object obj){
	           IPublicationAuthor pa = (PublicationAuthor) obj;
	           return pa.getPublication();
	       }
	    });
	    return publications;
	}
	

//	public void setPublications(List publications){
//	    List publicationsList = (List) CollectionUtils.collect(publications, new Transformer(){
//	        public Object transform(Object obj){
//	            IPublication publication = (IPublication) obj;
//	            IPublicationAuthor pa = new PublicationAuthor();
//	            pa.setAuthor(this);
//	            pa.setPublication(publication);
//	            pa.setOrder(publication.getOrderForAuthor(this));
//	        }
//	    });
//	}
	
	public String toString() {
		return "keyPerson: "+keyPerson+", person: "+person+", author: "+author+", organization: "+organization;
	}
	
	public boolean equals(IAuthor author){
	    if (author != null) {
	        if (this.keyPerson != null && author.getKeyPerson() != null){
	            return this.keyPerson == author.getKeyPerson();
	        }
            if (this.keyPerson == null || author.getKeyPerson() == null){
                return false;
            }
            if (this.getOrganization() == null && author.getOrganization() == null) {
                return this.getAuthor().equals(author.getAuthor());
            }
            else
                if (this.getOrganization() == null || author.getOrganization() == null)
                    return false;
                else return ( this.getAuthor().equals(author.getAuthor()) &&
                        	  this.getOrganization().equals(author.getOrganization()));
	    }
        return false;
	}

}
