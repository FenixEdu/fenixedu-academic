package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;

public class InfoAttribute extends InfoObject {

    private String attributeType;

    private List publications;

    public InfoAttribute() {
        super();
    }
    
    public void copyFromDomain(IAttribute att) {
        super.copyFromDomain(att);
        if (att != null) {
	        attributeType = att.getAttributeType();
	        publications = new ArrayList<IPublicationType>();
            
            for (IPublicationType publicationType : (List<IPublicationType>)att.getPublicationTypes()) {
                publications.add(InfoPublicationType.newInfoFromDomain(publicationType));
            }
        }
    }
    
    public static InfoAttribute newInfoFromDomain(IAttribute att) {
        InfoAttribute iatt = new InfoAttribute();
        if (att != null) {
            iatt.copyFromDomain(att);
        }
        return iatt;
    }

    /**
     * @return Returns the attributeType.
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * @param attributeType
     *            The attributeType to set.
     */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * @return Returns the publications.
     */
    public List getPublications() {
        return publications;
    }

    /**
     * @param publications
     *            The publications to set.
     */
    public void setPublications(List publications) {
        this.publications = publications;
    }
}