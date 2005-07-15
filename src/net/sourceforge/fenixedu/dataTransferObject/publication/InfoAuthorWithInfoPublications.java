package net.sourceforge.fenixedu.dataTransferObject.publication;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class InfoAuthorWithInfoPublications extends InfoAuthor {

    public void copyFromDomain(IPerson author) {
        if (author != null){
            super.copyFromDomain(author);
	        
            List publicationAuthors = new ArrayList(author.getPersonAuthorships());
            Collections.sort(publicationAuthors, new BeanComparator("order"));
            List publicationList = (List)CollectionUtils.collect(publicationAuthors,
                    new Transformer() {
                        public Object transform(Object object) {
                            IAuthorship authorship = (IAuthorship) object;
                            return authorship.getPublication();
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
    public void copyToDomain(InfoAuthorWithInfoPublications infoAuthor, IPerson author){
        if (infoAuthor != null && author != null){
            super.copyToDomain(infoAuthor, author);
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
