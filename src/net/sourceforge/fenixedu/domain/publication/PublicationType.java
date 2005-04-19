/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PublicationType extends PublicationType_Base implements IPublicationType {

    
    /**
     * @return Returns the nonRequiredAttributes.
     */
    public List getNonRequiredAttributes() {
        List nonRequiredAttributes = new ArrayList(getPublicationTypeAttributes());
        CollectionUtils.filter(nonRequiredAttributes, new Predicate(){

            public boolean evaluate(Object arg0) {
                IPublicationTypeAttribute publicationTypeAttribute = (IPublicationTypeAttribute) arg0;
                if(!publicationTypeAttribute.getRequired().booleanValue())
                    return true;
                return false;
            }            
        });
        
        CollectionUtils.transform(nonRequiredAttributes, new Transformer(){

            public Object transform(Object arg0) {
                IPublicationTypeAttribute publicationTypeAttribute = (IPublicationTypeAttribute) arg0;
                return publicationTypeAttribute.getAttribute();
            }            
        });
        return nonRequiredAttributes;
    }

    /**
     * @return Returns the nonRequiredAttributes.
     */
    public List getRequiredAttributes() {
        List requiredAttributes = new ArrayList(getPublicationTypeAttributes());
        CollectionUtils.filter(requiredAttributes, new Predicate(){

            public boolean evaluate(Object arg0) {
                IPublicationTypeAttribute publicationTypeAttribute = (IPublicationTypeAttribute) arg0;                
                if(publicationTypeAttribute.getRequired().booleanValue())
                    return true;
                return false;
            }            
        });

        CollectionUtils.transform(requiredAttributes, new Transformer(){

            public Object transform(Object arg0) {
                IPublicationTypeAttribute publicationTypeAttribute = (IPublicationTypeAttribute) arg0;
                return publicationTypeAttribute.getAttribute();
            }            
        });
        return requiredAttributes;
    }
   
}