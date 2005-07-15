package net.sourceforge.fenixedu.domain.publication;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class Attribute extends Attribute_Base implements IAttribute {

    public List getPublicationTypes() {
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