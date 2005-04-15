/*
 * Created on Apr 13, 2005
 *
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Ricardo Rodrigues
 *
 */

public class PublicationTypeAttribute extends DomainObject implements IPublicationTypeAttribute{
    
    private Integer keyPublicationType;
    private Integer keyAttribute;
    private IPublicationType publicationType;
    private IAttribute attribute;
    private Boolean required;
    
    
    public PublicationTypeAttribute() {
     
    }


    public IAttribute getAttribute() {
        return attribute;
    }    

    public void setAttribute(IAttribute attribute) {
        this.attribute = attribute;
    }
    
    public Integer getKeyAttribute() {
        return keyAttribute;
    }
    
    public void setKeyAttribute(Integer keyAttribute) {
        this.keyAttribute = keyAttribute;
    }   

    public Integer getKeyPublicationType() {
        return keyPublicationType;
    }
    
    public void setKeyPublicationType(Integer keyPublicationType) {
        this.keyPublicationType = keyPublicationType;
    }
    
    public IPublicationType getPublicationType() {
        return publicationType;
    }
    
    public void setPublicationType(IPublicationType publicationType) {
        this.publicationType = publicationType;
    }
    
    public Boolean getRequired() {
        return required;
    }
    
    public void setRequired(Boolean required) {
        this.required = required;
    }
    

}
