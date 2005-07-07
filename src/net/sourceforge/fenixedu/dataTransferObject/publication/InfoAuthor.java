/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;


import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InfoAuthor extends InfoObject {

	protected Integer keyPerson;
	public InfoPerson infoPessoa;
	public String author;
	public String organization;
	
	public List infoPublications;

	
    /* (non-Javadoc)
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IAuthor author) {
    	super.copyFromDomain(author);
        if (author != null){
	        setAuthor(author.getAuthor());
	        setIdInternal(author.getIdInternal());
	        setKeyPerson(author.getKeyPerson());
	        setOrganization(author.getOrganization());
        }
    }
  
    /*
     * Copies the infoAuthor to an Author object except the publications List for wich
     * @param 
     */
    public void copyToDomain(InfoAuthor infoAuthor, IAuthor author) throws ExcepcaoPersistencia{
        if (infoAuthor != null && author != null){
            super.copyToDomain(infoAuthor, author);
	        author.setAuthor(infoAuthor.getAuthor());
	        author.setKeyPerson(infoAuthor.getKeyPerson());
	        author.setOrganization(infoAuthor.getOrganization());
	        InfoPerson infoPerson = infoAuthor.getInfoPessoa();
	        if (infoAuthor.getKeyPerson() != null){
                
                ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		        IPerson pessoa = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,infoAuthor.getKeyPerson());
		        
		        author.setPerson(pessoa);
	        }
        }
    }
     
	public InfoAuthor(){
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
	 * @param organization The organization to set.
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
	 * @param publicationAuthors The publicationAuthors to set.
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
	 * @param infoPessoa The infoPessoa to set.
	 */
	public void setInfoPessoa(InfoPerson infoPessoa) {
		this.infoPessoa = infoPessoa;
	}

}
