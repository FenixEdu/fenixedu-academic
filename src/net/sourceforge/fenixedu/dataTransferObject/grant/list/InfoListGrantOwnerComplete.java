/*
 * Created on Jun 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;

/**
 * @author Pica
 * @author Pica
 */
public class InfoListGrantOwnerComplete extends InfoObject {

    private InfoGrantOwner infoGrantOwner;

    private List infoQualifications;

    private List infoListGrantContracts;

    /**
     * @return Returns the infoGrantOwner.
     */
    public InfoGrantOwner getInfoGrantOwner() {
        return infoGrantOwner;
    }

    /**
     * @param infoGrantOwner
     *            The infoGrantOwner to set.
     */
    public void setInfoGrantOwner(InfoGrantOwner infoGrantOwner) {
        this.infoGrantOwner = infoGrantOwner;
    }

    /**
     * @return Returns the infoListGrantContracts.
     */
    public List getInfoListGrantContracts() {
        return infoListGrantContracts;
    }

    /**
     * @param infoListGrantContracts
     *            The infoListGrantContracts to set.
     */
    public void setInfoListGrantContracts(List infoListGrantContracts) {
        this.infoListGrantContracts = infoListGrantContracts;
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