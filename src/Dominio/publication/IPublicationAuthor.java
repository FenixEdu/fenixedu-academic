/*
 * Created on 26/Out/2004
 */
package Dominio.publication;

import Dominio.IDomainObject;

/**
 * @author Ricardo Rodrigues
 */
public interface IPublicationAuthor extends IDomainObject{
    
    public IAuthor getAuthor();
    public void setAuthor(IAuthor author);
    public Integer getKeyAuthor();
    public void setKeyAuthor(Integer keyAuthor);
    
    public Integer getOrder();
    public void setOrder(Integer order);

    public Integer getKeyPublication();
    public void setKeyPublication(Integer keyPublication);
    public IPublication getPublication();
    public void setPublication(IPublication publication);

}
