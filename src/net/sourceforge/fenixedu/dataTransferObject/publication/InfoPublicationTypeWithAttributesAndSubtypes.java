package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.result.Attribute;
import net.sourceforge.fenixedu.domain.research.result.PublicationSubtype;
import net.sourceforge.fenixedu.domain.research.result.PublicationType;

public class InfoPublicationTypeWithAttributesAndSubtypes extends InfoPublicationType {

    public InfoPublicationTypeWithAttributesAndSubtypes() {
        super();
    }

    public void copyFromDomain(PublicationType pubType) {
        super.copyFromDomain(pubType);
        if (pubType != null) {
            
	        attributes = new ArrayList();
            
            for (Attribute attribute : (List<Attribute>)pubType.getRequiredAttributes()) {
                attributes.add(InfoAttribute.newInfoFromDomain(attribute));
            }
            
            for (Attribute attribute : (List<Attribute>)pubType.getNonRequiredAttributes()) {
                attributes.add(InfoAttribute.newInfoFromDomain(attribute));
            }
            
	        subtypes = new ArrayList();
            
            for (PublicationSubtype publicationSubtype : pubType.getSubtypes()) {
                subtypes.add(InfoPublicationSubtype.newInfoFromDomain(publicationSubtype));
            }
        }
    }
    
    public static InfoPublicationType newInfoFromDomain(PublicationType pubtype) {
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