/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.publication;

import java.util.List;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InfoAuthor extends InfoObject {

    private Integer keyPerson;

    public InfoPerson infoPessoa;

    public String author;

    public String organisation;

    public List authorPublications;

    public InfoAuthor() {

        super();
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
     * @param organisation
     *            The organisation to set.
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * @return Returns the publicationAuthors.
     */
    public List getAuthorPublications() {
        return authorPublications;
    }

    /**
     * @param publicationAuthors
     *            The publicationAuthors to set.
     */
    public void setAuthorPublications(List authorPublications) {
        this.authorPublications = authorPublications;
    }

    /**
     * @return Returns the infoPessoa.
     */
    public InfoPerson getInfoPessoa() {
        return infoPessoa;
    }

    /**
     * @param infoPessoa
     *            The infoPessoa to set.
     */
    public void setInfoPessoa(InfoPerson infoPessoa) {
        this.infoPessoa = infoPessoa;
    }

}