/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.List;

import Dominio.IDomainObject;
import Dominio.IPerson;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IAuthor extends IDomainObject {
    /**
     * @return Returns the author.
     */
    public abstract String getAuthor();

    /**
     * @return Returns the keyPerson.
     */
    public abstract Integer getKeyPerson();

    /**
     * @return Returns the organization.
     */
    public abstract String getOrganization();

    /**
     * @return Returns the pessoa.
     */
    public abstract IPerson getPerson();

    /**
     * @param author
     *            The author to set.
     */
    public abstract void setAuthor(String author);

    /**
     * @param keyPerson
     *            The keyPerson to set.
     */
    public abstract void setKeyPerson(Integer keyPerson);

    /**
     * @param organization
     *            The organization to set.
     */
    public abstract void setOrganization(String organization);

    /**
     * @param pessoa
     *            The pessoa to set.
     */
    public abstract void setPerson(IPerson person);

    /**
     * @return the List of Publications from the author
     */
    public List getPublications();
    
    public List getAuthorPublications();

    /**
     * @param publications the list of publications to be set as being of the author
     */
   // public void setPublications(List publications);
    
}