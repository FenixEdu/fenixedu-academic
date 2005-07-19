/*
 * Created on 18/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoGrantContract extends InfoObject {

    private Integer contractNumber;

    private String endContractMotive;

    private Integer costCenterKey;

    private InfoGrantOwner grantOwnerInfo;

    private InfoGrantCostCenter grantCostCenterInfo;

    private InfoGrantOrientationTeacher grantOrientationTeacherInfo;

    private InfoGrantType grantTypeInfo;

    private Date dateAcceptTerm;

    private Boolean active;

    /*
     * If a contract motive is set, the contract is finished. If not, the
     * contract is active and running.
     */
    public boolean getEndContractMotiveSet() {
        if (endContractMotive == null || endContractMotive.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    public Integer getContractNumber() {
        return contractNumber;
    }

    /**
     * @param contractNumber
     */
    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @return
     */
    public String getEndContractMotive() {
        return endContractMotive;
    }

    /**
     * @param endContractMotive
     */
    public void setEndContractMotive(String endContractMotive) {
        this.endContractMotive = endContractMotive;
    }

    /**
     * @return
     */
    public InfoGrantOwner getGrantOwnerInfo() {
        return grantOwnerInfo;
    }

    /**
     * @param grantOwnerInfo
     */
    public void setGrantOwnerInfo(InfoGrantOwner grantOwnerInfo) {
        this.grantOwnerInfo = grantOwnerInfo;
    }

    /**
     * @return Returns the grantOrientationTeacherInfo.
     */
    public InfoGrantOrientationTeacher getGrantOrientationTeacherInfo() {
        return grantOrientationTeacherInfo;
    }

    /**
     * @param grantOrientationTeacherInfo
     *            The grantOrientationTeacherInfo to set.
     */
    public void setGrantOrientationTeacherInfo(InfoGrantOrientationTeacher grantOrientationTeacherInfo) {
        this.grantOrientationTeacherInfo = grantOrientationTeacherInfo;
    }

    /**
     * @return Returns the grantTypeInfo.
     */
    public InfoGrantType getGrantTypeInfo() {
        return grantTypeInfo;
    }

    /**
     * @param grantTypeInfo
     *            The grantTypeInfo to set.
     */
    public void setGrantTypeInfo(InfoGrantType grantTypeInfo) {
        this.grantTypeInfo = grantTypeInfo;
    }

    /**
     * @return Returns the dateAcceptTerm.
     */
    public Date getDateAcceptTerm() {
        return dateAcceptTerm;
    }

    /**
     * @param dateAcceptTerm
     *            The dateAcceptTerm to set.
     */
    public void setDateAcceptTerm(Date dateAcceptTerm) {
        this.dateAcceptTerm = dateAcceptTerm;
    }

    /**
     * @return Returns the active.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active
     *            The active to set.
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean getContractActive() {
        if (active == null) {
            return false;
        }
        return active.booleanValue();
    }

    /**
     * @param GrantContract
     */
    public void copyFromDomain(IGrantContract grantContract) {
        super.copyFromDomain(grantContract);
        if (grantContract != null) {
            setContractNumber(grantContract.getContractNumber());
            setEndContractMotive(grantContract.getEndContractMotive());
            setDateAcceptTerm(grantContract.getDateAcceptTerm());
        }
    }

    /**
     * @param GrantContract
     * @return
     */
    public static InfoGrantContract newInfoFromDomain(IGrantContract grantContract) {
        InfoGrantContract infoGrantContract = null;
        if (grantContract != null) {
            infoGrantContract = new InfoGrantContract();
            infoGrantContract.copyFromDomain(grantContract);
        }
        return infoGrantContract;
    }

    public void copyToDomain(InfoGrantContract infoGrantContract, IGrantContract grantContract)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantContract, grantContract);

        grantContract.setContractNumber(infoGrantContract.getContractNumber());
        grantContract.setDateAcceptTerm(infoGrantContract.getDateAcceptTerm());
        grantContract.setEndContractMotive(infoGrantContract.getEndContractMotive());
        if (infoGrantContract.getGrantCostCenterInfo() != null) {
            grantContract.setCostCenterKey(infoGrantContract.getGrantCostCenterInfo().getIdInternal());
        }
    }

    /**
     * @return Returns the costCenterKey.
     */
    public Integer getCostCenterKey() {
        return costCenterKey;
    }

    /**
     * @param costCenterKey
     *            The costCenterKey to set.
     */
    public void setCostCenterKey(Integer costCenterKey) {
        this.costCenterKey = costCenterKey;
    }

    /**
     * @return Returns the grantCostCenterInfo.
     */
    public InfoGrantCostCenter getGrantCostCenterInfo() {
        return grantCostCenterInfo;
    }

    /**
     * @param grantCostCenterInfo
     *            The grantCostCenterInfo to set.
     */
    public void setGrantCostCenterInfo(InfoGrantCostCenter grantCostCenterInfo) {
        this.grantCostCenterInfo = grantCostCenterInfo;
    }

}
