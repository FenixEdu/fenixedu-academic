/*
 * Created on 18/Nov/2003
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.grant.owner.InfoGrantOwner;
import Dominio.grant.contract.IGrantContract;

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
    
    /**
     * @param GrantContract
     */
    public void copyFromDomain(IGrantContract grantContract)
    {
    	super.copyFromDomain(grantContract);
    	if (grantContract != null)
    	{
    		setContractNumber(grantContract.getContractNumber());
    		setEndContractMotive(grantContract.getEndContractMotive());
    		setDateAcceptTerm(grantContract.getDateAcceptTerm());
    	}
    }
    /**
     * @param GrantContract
     * @return
     */
    public static InfoGrantContract newInfoFromDomain(IGrantContract grantContract)
    {
    	InfoGrantContract infoGrantContract = null;
    	if (grantContract != null)
    	{
    		infoGrantContract = new InfoGrantContract();
    		infoGrantContract.copyFromDomain(grantContract);
    	}
    	return infoGrantContract;
    }
    
}