/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InfoPublicationFormat extends InfoObject {

    private String format;

    public InfoPublicationFormat() {
        super();
    }

    /**
     * @return Returns the subtype.
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param subtype
     *            The subtype to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }

}