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
import org.apache.commons.collections.Transformer;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Attribute extends Attribute_Base implements IAttribute {

    /**
     * @return Returns the publications.
     */
    public List getPublications() {
        List publicationTypes = new ArrayList(getPublicationTypeAttributes());
        CollectionUtils.transform(publicationTypes,new Transformer(){

            public Object transform(Object arg0) {
                IPublicationTypeAttribute publicationTypeAttribute = (IPublicationTypeAttribute) arg0;
                return publicationTypeAttribute.getPublicationType();
            }
            
        });
        return publicationTypes;
    }    
}