/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.List;

import Dominio.IDomainObject;
import Dominio.IPessoa;

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
     * @return Returns the organisation.
     */
    public abstract String getOrganisation();

    /**
     * @return Returns the pessoa.
     */
    public abstract IPessoa getPerson();

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
     * @param organisation
     *            The organisation to set.
     */
    public abstract void setOrganisation(String organisation);

    /**
     * @param pessoa
     *            The pessoa to set.
     */
    public abstract void setPerson(IPessoa person);

    /**
     * @return Returns the publicationAuthors.
     */
    public abstract List getAuthorPublications();

    /**
     * @param publicationAuthors
     *            The publicationAuthors to set.
     */
    public abstract void setAuthorPublications(List authorPublications);
}