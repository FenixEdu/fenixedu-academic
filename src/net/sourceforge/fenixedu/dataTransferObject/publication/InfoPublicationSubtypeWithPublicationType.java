
package net.sourceforge.fenixedu.dataTransferObject.publication;

import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;

public class InfoPublicationSubtypeWithPublicationType extends InfoPublicationSubtype {

    public InfoPublicationSubtypeWithPublicationType() {
        super();
    }
    
    public void copyFromDomain(IPublicationSubtype pubSubtype) {
        super.copyFromDomain(pubSubtype);
        keyPublicationType = pubSubtype.getKeyPublicationType();
        subtype = pubSubtype.getSubtype();
        publicationType = InfoPublicationType.newInfoFromDomain(pubSubtype.getPublicationType());
    }
    
    public static InfoPublicationSubtype newInfoFromDomain(IPublicationSubtype pubSubtype) {        
        InfoPublicationSubtypeWithPublicationType infoSubtype = new InfoPublicationSubtypeWithPublicationType();
        infoSubtype.copyFromDomain(pubSubtype);
        return infoSubtype;
    }

    /**
     * @return Returns the keyPublicationType.
     */
    public Integer getKeyPublicationType() {
        return keyPublicationType;
    }

    /**
     * @param keyPublicationType
     *            The keyPublicationType to set.
     */
    public void setKeyPublicationType(Integer keyPublicationType) {
        this.keyPublicationType = keyPublicationType;
    }

    /**
     * @return Returns the publicationType.
     */
    public InfoPublicationType getPublicationType() {
        return publicationType;
    }

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public void setPublicationType(InfoPublicationType publicationType) {
        this.publicationType = publicationType;
    }

    /**
     * @return Returns the subtype.
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * @param subtype
     *            The subtype to set.
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}