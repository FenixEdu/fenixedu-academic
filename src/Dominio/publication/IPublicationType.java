/*
 * Created on May 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.List;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface IPublicationType {
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
	 * @param nonRequiredAttributes The nonRequiredAttributes to set.
	 */
	public abstract void setNonRequiredAttributes(List nonRequiredAttributes);
	/**
	 * @param publicationType The publicationType to set.
	 */
	public abstract void setPublicationType(String publicationType);
	/**
	 * @param requiredAttributes The requiredAttributes to set.
	 */
	public abstract void setRequiredAttributes(List requiredAttributes);
	/**
	 * @param subtypes The subtypes to set.
	 */
	public abstract void setSubtypes(List subtypes);
}