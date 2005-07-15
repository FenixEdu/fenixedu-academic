package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;

public class InfoPublicationType extends InfoObject {

    protected String publicationType;

    protected List attributes;

    protected List subtypes;

    public InfoPublicationType() {
        super();
    }

    public void copyFromDomain(IPublicationType pubType) {
        super.copyFromDomain(pubType);
        if (pubType != null) {
	        publicationType = pubType.getPublicationType();
        }
    }
    
    public static InfoPublicationType newInfoFromDomain(IPublicationType pubtype) {
        InfoPublicationType iptype = new InfoPublicationType();
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