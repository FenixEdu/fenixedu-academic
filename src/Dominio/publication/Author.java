/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;


import java.util.List;

import Dominio.DomainObject;
import Dominio.IPessoa;
/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Author extends DomainObject implements IAuthor{

	private Integer keyPerson;
	public IPessoa person;
	public String author;
	public String organisation;
	
	public List authorPublications;
	
	public Author(){	
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
	 * @return Returns the organisation.
	 */
	public String getOrganisation() {
		return organisation;
	}

	/**
	 * @return Returns the pessoa.
	 */
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * @param author The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param keyPerson The keyPerson to set.
	 */
	public void setKeyPerson(Integer keyPerson) {
		this.keyPerson = keyPerson;
	}

	/**
	 * @param organisation The organisation to set.
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	/**
	 * @param pessoa The pessoa to set.
	 */
	public void setPerson(IPessoa person) {
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

}
