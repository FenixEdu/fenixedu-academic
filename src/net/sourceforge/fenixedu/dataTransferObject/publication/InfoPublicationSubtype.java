/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InfoPublicationSubtype extends InfoObject {

    protected Integer keyPublicationType;

    protected String subtype;

    protected InfoPublicationType publicationType;

    public InfoPublicationSubtype() {
        super();
    }
    
    public void copyFromDomain(IPublicationSubtype pubSubtype) {
        super.copyFromDomain(pubSubtype);
        keyPublicationType = pubSubtype.getKeyPublicationType();
        subtype = pubSubtype.getSubtype();
    }
    
    public static InfoPublicationSubtype newInfoFromDomain(IPublicationSubtype pubSubtype) {
        InfoPublicationSubtype infoSubtype = new InfoPublicationSubtype();
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