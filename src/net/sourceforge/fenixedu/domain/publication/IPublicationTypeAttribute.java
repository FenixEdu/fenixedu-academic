/*
 * Created on Apr 13, 2005
 *
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Ricardo Rodrigues
 *
 */

public interface IPublicationTypeAttribute extends IDomainObject{
    
    public IAttribute getAttribute();

    public void setAttribute(IAttribute attribute);
    
    public Integer getKeyAttribute();
    
    public void setKeyAttribute(Integer keyAttribute);

    public Integer getKeyPublicationType();
    
    public void setKeyPublicationType(Integer keyPublicationType);
    
    public IPublicationType getPublicationType();
    
    public void setPublicationType(IPublicationType publicationType);
    
    public Boolean getRequired();
    
    public void setRequired(Boolean required);    

}
