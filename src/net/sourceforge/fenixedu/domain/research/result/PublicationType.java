package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public class PublicationType extends PublicationType_Base {

    public PublicationType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    /**
     * @return Returns the nonRequiredAttributes.
     */
    public List getNonRequiredAttributes() {
        List nonRequiredAttributes = new ArrayList(getPublicationTypeAttributes());
        CollectionUtils.filter(nonRequiredAttributes, new Predicate(){

            public boolean evaluate(Object arg0) {
                PublicationTypeAttribute publicationTypeAttribute = (PublicationTypeAttribute) arg0;
                if(!publicationTypeAttribute.getRequired().booleanValue())
                    return true;
                return false;
            }            
        });
        
        CollectionUtils.transform(nonRequiredAttributes, new Transformer(){

            public Object transform(Object arg0) {
                PublicationTypeAttribute publicationTypeAttribute = (PublicationTypeAttribute) arg0;
                return publicationTypeAttribute.getAttribute();
            }            
        });
        return nonRequiredAttributes;
    }

    /**
     * @return Returns the requiredAttributes.
     */
    public List getRequiredAttributes() {
        List requiredAttributes = new ArrayList(getPublicationTypeAttributes());
        CollectionUtils.filter(requiredAttributes, new Predicate(){

            public boolean evaluate(Object arg0) {
                PublicationTypeAttribute publicationTypeAttribute = (PublicationTypeAttribute) arg0;                
                if(publicationTypeAttribute.getRequired().booleanValue())
                    return true;
                return false;
            }            
        });

        CollectionUtils.transform(requiredAttributes, new Transformer(){

            public Object transform(Object arg0) {
                PublicationTypeAttribute publicationTypeAttribute = (PublicationTypeAttribute) arg0;
                return publicationTypeAttribute.getAttribute();
            }            
        });
        return requiredAttributes;
    }
   
}