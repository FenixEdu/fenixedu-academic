/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.publication;

import DataBeans.InfoObject;
import Dominio.publication.IPublicationType;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InfoPublicationSubtype extends InfoObject {
	
	private Integer keyPublicationType;
    private String subtype;
    private IPublicationType publicationType;

	public InfoPublicationSubtype() {
		super();
	}
	
	
    
    /**
     * @return Returns the keyPublicationType.
     */
    public Integer getKeyPublicationType()
    {
        return keyPublicationType;
    }
    /**
     * @param keyPublicationType The keyPublicationType to set.
     */
    public void setKeyPublicationType(Integer keyPublicationType)
    {
        this.keyPublicationType = keyPublicationType;
    }
    /**
     * @return Returns the publicationType.
     */
    public IPublicationType getPublicationType()
    {
        return publicationType;
    }
    /**
     * @param publicationType The publicationType to set.
     */
    public void setPublicationType(IPublicationType publicationType)
    {
        this.publicationType = publicationType;
    }
    /**
     * @return Returns the subtype.
     */
    public String getSubtype()
    {
        return subtype;
    }
    /**
     * @param subtype The subtype to set.
     */
    public void setSubtype(String subtype)
    {
        this.subtype = subtype;
    }
}