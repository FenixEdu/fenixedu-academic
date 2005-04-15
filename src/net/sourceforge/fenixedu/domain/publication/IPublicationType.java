/*
 * Created on May 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IPublicationType extends IDomainObject {
    /**
     * @return Returns the nonRequiredAttributes.
     */
    public abstract List getNonRequiredAttributes();

    /**
     * @return Returns the publicationType.
     */
    public abstract String getPublicationType();

    /**
     * @return Returns the requiredAttributes.
     */
    public abstract List getRequiredAttributes();

    /**
     * @return Returns the subtypes.
     */
    public abstract List getSubtypes();

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public abstract void setPublicationType(String publicationType);


    /**
     * @param subtypes
     *            The subtypes to set.
     */
    public abstract void setSubtypes(List subtypes);
    
    public abstract List getPublicationTypeAttributes();
    
    public abstract void setPublicationTypeAttributes(List publicationTypeAttributes);
}