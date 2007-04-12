package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;

public class InfoAuthor extends InfoObject {

    protected Integer keyPerson;

    public InfoPerson infoPessoa;

    public String author;

    public String organization;

    public List infoPublications;

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(Person author) {
        super.copyFromDomain(author);
        if (author != null) {
            setAuthor(author.getName());
            setIdInternal(author.getIdInternal());
            setKeyPerson(author.getIdInternal());
            setOrganization((author.getExternalPerson() != null) ? (author.getExternalPerson()
                    .getInstitutionUnit().getName() + "     (Pessoa externa)") : "");
        }
    }

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
     * @return Returns the organization.
     */
    public String getOrganization() {
        return organization;
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
     * @return Returns the publicationAuthors.
     */
    public List getInfoPublications() {
        return infoPublications;
    }

    /**
     * @param publicationAuthors
     *            The publicationAuthors to set.
     */
    public void setInfoPublications(List infoPublications) {
        this.infoPublications = infoPublications;
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
