/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoObject;
import Dominio.publication.IAttribute;
import Dominio.publication.IPublicationType;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
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
	        publications = new ArrayList();
	        IPublicationType pubtype = null;
	        for (Iterator iter = att.getPublications().iterator(); iter.hasNext(); pubtype = (IPublicationType)iter.next()) 
	        {
	            InfoPublicationType ipubtype = InfoPublicationType.newInfoFromDomain(pubtype);
	            publications.add(ipubtype);
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