/*
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteQualifications extends InfoObject {
    List infoQualifications;

    InfoPerson infoPerson;

    /**
     *  
     */
    public InfoSiteQualifications() {
    }

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @param infoPerson
     *            The infoPerson to set.
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * @return Returns the infoQualifications.
     */
    public List getInfoQualifications() {
        return infoQualifications;
    }

    /**
     * @param infoQualifications
     *            The infoQualifications to set.
     */
    public void setInfoQualifications(List infoQualifications) {
        this.infoQualifications = infoQualifications;
    }
}