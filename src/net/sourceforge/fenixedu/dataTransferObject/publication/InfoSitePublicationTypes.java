/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;

/**
 * @author Sairf
 *  
 */
public class InfoSitePublicationTypes extends DataTranferObject implements ISiteComponent {

    private List infoPublicationTypes;

    public InfoSitePublicationTypes() {
    }

    /**
     * @return Returns the infoPublicationTypes.
     */
    public List getInfoPublicationTypes() {
        return infoPublicationTypes;
    }

    /**
     * @param infoPublicationTypes
     *            The infoPublicationTypes to set.
     */
    public void setInfoPublicationTypes(List infoPublicationTypes) {
        this.infoPublicationTypes = infoPublicationTypes;
    }
}