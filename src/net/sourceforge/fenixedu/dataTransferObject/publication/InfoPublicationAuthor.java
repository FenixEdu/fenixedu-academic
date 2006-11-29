/*
 * Created on May 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;


import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.publication.Authorship;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class InfoPublicationAuthor extends InfoObject {
    
    private Integer idInternal;
    private Integer keyPublication;
    private Integer keyAuthor;
    private Integer order;
    private InfoAuthor infoAuthor;
    private InfoPublication infoPublication;
    
    /* (non-Javadoc)
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(Authorship publicationAuthor) {
        super.copyFromDomain(publicationAuthor);
        if (publicationAuthor != null){
	        this.setKeyAuthor(publicationAuthor.getKeyAuthor());
	        this.setKeyPublication(publicationAuthor.getKeyPublication());
	        this.setOrder(publicationAuthor.getAuthorOrder());
	        InfoAuthor infoAuthor = new InfoAuthor();
	        infoAuthor.copyFromDomain(publicationAuthor.getAuthor());
	        this.setInfoAuthor(infoAuthor);
	        InfoPublication infoPublication = new InfoPublication();
	        infoPublication.copyFromDomain(publicationAuthor.getPublication());
	        this.setInfoPublication(infoPublication);
        }
    }
  
    /* Does not copy Object's, merely primitive type variables */
    public void copyToDomain(InfoPublicationAuthor infoPublicationAuthor, Authorship publicationAuthor){
        if (infoPublicationAuthor != null && publicationAuthor != null){
            super.copyToDomain(infoPublicationAuthor, publicationAuthor);
            publicationAuthor.setKeyAuthor(infoPublicationAuthor.getKeyAuthor());
            publicationAuthor.setKeyPublication(infoPublicationAuthor.getKeyPublication());
            publicationAuthor.setAuthorOrder(infoPublicationAuthor.getOrder());
        }
    }
       
	public InfoPublicationAuthor(){
		super();
	}    

    /**
     * @return Returns the idInternal.
     */
    public Integer getIdInternal() {
        return idInternal;
    }
    /**
     * @param idInternal The idInternal to set.
     */
    public void setIdInternal(Integer idInternal) {
        this.idInternal = idInternal;
    }
    /**
     * @return Returns the keyAuthor.
     */
    public Integer getKeyAuthor() {
        return keyAuthor;
    }
    /**
     * @param keyAuthor The keyAuthor to set.
     */
    public void setKeyAuthor(Integer keyAuthor) {
        this.keyAuthor = keyAuthor;
    }
    /**
     * @return Returns the keyPublication.
     */
    public Integer getKeyPublication() {
        return keyPublication;
    }
    /**
     * @param keyPublication The keyPublication to set.
     */
    public void setKeyPublication(Integer keyPublication) {
        this.keyPublication = keyPublication;
    }
    /**
     * @return Returns the order.
     */
    public Integer getOrder() {
        return order;
    }
    /**
     * @param order The order to set.
     */
    public void setOrder(Integer order) {
        this.order = order;
    }
    /**
     * @return Returns the infoPublication.
     */
    public InfoPublication getInfoPublication() {
        return infoPublication;
    }
    /**
     * @param infoPublication The infoPublication to set.
     */
    public void setInfoPublication(InfoPublication infoPublication) {
        this.infoPublication = infoPublication;
    }
    /**
     * @return Returns the infoAuthor.
     */
    public InfoAuthor getInfoAuthor() {
        return infoAuthor;
    }
    /**
     * @param infoAuthor The infoAuthor to set.
     */
    public void setInfoAuthor(InfoAuthor infoAuthor) {
        this.infoAuthor = infoAuthor;
    }
}
