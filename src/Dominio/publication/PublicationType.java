/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.List;

import Dominio.DomainObject;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PublicationType extends DomainObject implements IPublicationType {

    private String publicationType;

    private List requiredAttributes;

    private List nonRequiredAttributes;

    private List subtypes;

    /**
     *  
     */
    public PublicationType() {
    }

    /**
     * @return Returns the nonRequiredAttributes.
     */
    public List getNonRequiredAttributes() {
        return nonRequiredAttributes;
    }

    /**
     * @return Returns the publicationType.
     */
    public String getPublicationType() {
        return publicationType;
    }

    /**
     * @return Returns the requiredAttributes.
     */
    public List getRequiredAttributes() {
        return requiredAttributes;
    }

    /**
     * @return Returns the subtypes.
     */
    public List getSubtypes() {
        return subtypes;
    }

    /**
     * @param nonRequiredAttributes
     *            The nonRequiredAttributes to set.
     */
    public void setNonRequiredAttributes(List nonRequiredAttributes) {
        this.nonRequiredAttributes = nonRequiredAttributes;
    }

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    /**
     * @param requiredAttributes
     *            The requiredAttributes to set.
     */
    public void setRequiredAttributes(List requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    /**
     * @param subtypes
     *            The subtypes to set.
     */
    public void setSubtypes(List subtypes) {
        this.subtypes = subtypes;
    }

}