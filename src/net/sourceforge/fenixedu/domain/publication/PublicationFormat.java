/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PublicationFormat extends DomainObject implements IPublicationFormat {

    private String format;

    /**
     *  
     */
    public PublicationFormat() {
    }

    /**
     * @return Returns the format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format
     *            The format to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }

}