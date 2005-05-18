/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InfoAuthorWithInfoPublications extends InfoAuthor {

	
    /* (non-Javadoc)
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IAuthor author) {
        if (author != null){
            super.copyFromDomain(author);
	        setAuthor(author.getAuthor());
	        setIdInternal(author.getIdInternal());
	        setKeyPerson(author.getKeyPerson());
	        setOrganization(author.getOrganization());
	        
            List publicationAuthors = new ArrayList(author.getAuthorPublications());
            Collections.sort(publicationAuthors, new BeanComparator("order"));
            List publicationList = (List)CollectionUtils.collect(publicationAuthors,
                    new Transformer() {
                        public Object transform(Object object) {
                            PublicationAuthor publicationAuthor = (PublicationAuthor) object;
                            return publicationAuthor.getPublication();
            }});
            
	        List infoPublicationsList = new ArrayList();
            Iterator it = publicationList.iterator();
	        while (it.hasNext()){
	            IPublication publication = (IPublication)it.next();
	            InfoPublication infoPublication = new InfoPublication();
	            infoPublication.copyFromDomain(publication);
	            infoPublicationsList.add(infoPublication);
	        }
        }
    }
  
    /*
     * Copies the infoAuthor to an Author object except the publications List for wich
     * @param 
     */
    public void copyToDomain(InfoAuthorWithInfoPublications infoAuthor, IAuthor author){
        if (infoAuthor != null && author != null){
            super.copyToDomain(infoAuthor, author);
	        author.setAuthor(infoAuthor.getAuthor());
	        author.setKeyPerson(infoAuthor.getKeyPerson());
	        author.setOrganization(infoAuthor.getOrganization());
	        InfoPerson infoPerson = infoAuthor.getInfoPessoa();
	        if (keyPerson != null){
		        IPerson pessoa = new Person();
		        infoPerson.copyToDomain(infoPerson, pessoa);
		        author.setPerson(pessoa);
	        }
        }
    }
    
	public InfoAuthorWithInfoPublications(){
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
