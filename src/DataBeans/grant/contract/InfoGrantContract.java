/*
 * Created on 18/Nov/2003
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.grant.owner.InfoGrantOwner;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoGrantContract extends InfoObject {

    private Integer contractNumber;
    private String endContractMotive;
    private InfoGrantOwner grantOwnerInfo;
    private InfoGrantOrientationTeacher grantOrientationTeacherInfo;
    private InfoGrantType grantTypeInfo;
    private Date dateAcceptTerm;

    /*
     * If a contract motive is set, the contract is finished. 
     * If not, the contract is active and running.
     */
    public boolean getEndContractMotiveSet() {
        if (endContractMotive == null || endContractMotive.equals(""))
            return false;
        else
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
    public void setGrantOrientationTeacherInfo(
            InfoGrantOrientationTeacher grantOrientationTeacherInfo) {
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
}