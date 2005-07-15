package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;

public class InfoPublicationTypeWithAttributesAndSubtypes extends InfoPublicationType {

    public InfoPublicationTypeWithAttributesAndSubtypes() {
        super();
    }

    public void copyFromDomain(IPublicationType pubType) {
        super.copyFromDomain(pubType);
        if (pubType != null) {
            
	        attributes = new ArrayList();
            
            for (IAttribute attribute : (List<IAttribute>)pubType.getRequiredAttributes()) {
                attributes.add(InfoAttribute.newInfoFromDomain(attribute));
            }
            
            for (IAttribute attribute : (List<IAttribute>)pubType.getNonRequiredAttributes()) {
                attributes.add(InfoAttribute.newInfoFromDomain(attribute));
            }
            
	        subtypes = new ArrayList();
            
            for (IPublicationSubtype publicationSubtype : pubType.getSubtypes()) {
                subtypes.add(InfoPublicationSubtype.newInfoFromDomain(publicationSubtype));
            }
        }
    }
    
    public static InfoPublicationType newInfoFromDomain(IPublicationType pubtype) {
        InfoPublicationTypeWithAttributesAndSubtypes iptype = new InfoPublicationTypeWithAttributesAndSubtypes();
        iptype.copyFromDomain(pubtype);
        return iptype;
    }
    
    /**
     * @return Returns the attributes.
     */
    public List getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            The attributes to set.
     */
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    /**
     * @return Returns the publicationType.
     */
    public String getPublicationType() {
        return publicationType;
    }

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    /**
     * @return Returns the subtypes.
     */
    public List getSubtypes() {
        return subtypes;
    }

    /**
     * @param subtypes
     *            The subtypes to set.
     */
    public void setSubtypes(List subtypes) {
        this.subtypes = subtypes;
    }
}