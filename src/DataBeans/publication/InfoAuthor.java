/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.publication;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;
import Dominio.IPerson;
import Dominio.Person;
import Dominio.publication.Author;
import Dominio.publication.IAuthor;
import Dominio.publication.IPublication;
import Dominio.publication.Publication;

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
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
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
    public void copyToDomain(InfoAuthor infoAuthor, IAuthor author){
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
    
    public static IAuthor newDomainFromInfo(InfoAuthor infoAuthor){
        IAuthor author = null;
        if(infoAuthor != null){
            author = new Author();
            infoAuthor.copyToDomain(infoAuthor, author);
        }
        return author;
    }
       
    /**
     * @param infoAuthor is the InfoAuthor from wich the publications will be
     * retrieved
     * @return a the list of infoPublications that belong to the author
     */
    public static List copyPublicationsFromInfo(InfoAuthor infoAuthor){
        
        Iterator it = infoAuthor.getInfoPublications().iterator();
        List listaInfoPublicacoes = new ArrayList();
        while(it.hasNext()){
            InfoPublication infoPublication = (InfoPublication) it.next();
            IPublication publication = new Publication();
            infoPublication.copyToDomain(infoPublication, publication);
            listaInfoPublicacoes.add(publication);
        }
        return listaInfoPublicacoes;
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
