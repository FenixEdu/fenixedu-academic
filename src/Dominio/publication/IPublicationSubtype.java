/*
 * Created on 4/Mai/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import Dominio.IDomainObject;

/**
 * @author _Sairf_
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IPublicationSubtype extends IDomainObject {

    /**
     * @return Returns the keyPublicationType.
     */
    public abstract Integer getKeyPublicationType();

    /**
     * @param keyPublicationType
     *            The keyPublicationType to set.
     */
    public abstract void setKeyPublicationType(Integer keyPublicationType);

    /**
     * @return Returns the publicationType.
     */
    public abstract IPublicationType getPublicationType();

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public abstract void setPublicationType(IPublicationType publicationType);

    /**
     * @return Returns the subtype.
     */
    public abstract String getSubtype();

    /**
     * @param subtype
     *            The subtype to set.
     */
    public abstract void setSubtype(String subtype);
}