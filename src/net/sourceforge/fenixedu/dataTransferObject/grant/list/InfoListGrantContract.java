/*
 * Created on Jun 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantContract extends InfoObject {

    private InfoGrantContract infoGrantContract;

    private List infoGrantContractRegimes; //The actual regime is in the first

    // position

    private List infoListGrantSubsidys; // The actual subsidy is in the first

    // position

    /**
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return infoGrantContract;
    }

    /**
     * @param infoGrantContract
     *            The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
    }

    /**
     * @return Returns the infoGrantContractRegimes.
     */
    public List getInfoGrantContractRegimes() {
        return infoGrantContractRegimes;
    }

    /**
     * @param infoGrantContractRegimes
     *            The infoGrantContractRegimes to set.
     */
    public void setInfoGrantContractRegimes(List infoGrantContractRegimes) {
        this.infoGrantContractRegimes = infoGrantContractRegimes;
    }

    /**
     * @return Returns the infoListGrantSubsidys.
     */
    public List getInfoListGrantSubsidys() {
        return infoListGrantSubsidys;
    }

    /**
     * @param infoListGrantSubsidys
     *            The infoListGrantSubsidys to set.
     */
    public void setInfoListGrantSubsidys(List infoListGrantSubsidys) {
        this.infoListGrantSubsidys = infoListGrantSubsidys;
    }
}