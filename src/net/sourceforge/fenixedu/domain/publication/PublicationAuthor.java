/*
 * Created on 26/Out/2004
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Ricardo Rodrigues
 */
public class PublicationAuthor extends DomainObject implements IPublicationAuthor{
    
    private Integer idInternal;
    private Integer keyPublication;
    private Integer keyAuthor;
    private Integer order;
    private IAuthor author;
    private IPublication publication;
    
    /**
     * 
     */
    public PublicationAuthor() {     
    }

    /**
     * @return Returns the author.
     */
    public IAuthor getAuthor() {
        return author;
    }
    /**
     * @param author The author to set.
     */
    public void setAuthor(IAuthor author) {
        this.author = author;
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
     * @return Returns the publication.
     */
    public IPublication getPublication() {
        return publication;
    }
    /**
     * @param publication The publication to set.
     */
    public void setPublication(IPublication publication) {
        this.publication = publication;
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
}
