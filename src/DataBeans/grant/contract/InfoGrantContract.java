/*
 * Created on 18/Nov/2003
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.grant.owner.InfoGrantOwner;

/**
 * @author  Barbosa
 * @author  Pica
 * 
 */

public class InfoGrantContract extends InfoObject
{
    private Integer contractNumber;
	private Date dateBeginContract;
	private Date dateEndContract;
    private String endContractMotive;
	private InfoGrantOwner grantOwnerInfo;
    private InfoGrantType grantTypeInfo;
    private InfoGrantResponsibleTeacher grantResponsibleTeacherInfo;
	private InfoGrantOrientationTeacher grantOrientationTeacherInfo;
    
    public InfoGrantContract()
    {
    }

    /**
     * @return
     */
    public Integer getContractNumber()
    {
        return contractNumber;
    }

    /**
     * @param contractNumber
     */
    public void setContractNumber(Integer contractNumber)
    {
        this.contractNumber = contractNumber;
    }

    /**
     * @return
     */
    public Date getDateBeginContract()
    {
        return dateBeginContract;
    }

    /**
     * @param dateBeginContract
     */
    public void setDateBeginContract(Date dateBeginContract)
    {
        this.dateBeginContract = dateBeginContract;
    }

    /**
     * @return
     */
    public Date getDateEndContract()
    {
        return dateEndContract;
    }

    /**
     * @param dateEndContract
     */
    public void setDateEndContract(Date dateEndContract)
    {
        this.dateEndContract = dateEndContract;
    }

    /**
     * @return
     */
    public String getEndContractMotive()
    {
        return endContractMotive;
    }

    /**
     * @param endContractMotive
     */
    public void setEndContractMotive(String endContractMotive)
    {
        this.endContractMotive = endContractMotive;
    }

    /**
     * @return
     */
    public InfoGrantOwner getGrantOwnerInfo()
    {
        return grantOwnerInfo;
    }

    /**
     * @param grantOwnerInfo
     */
    public void setGrantOwnerInfo(InfoGrantOwner grantOwnerInfo)
    {
        this.grantOwnerInfo = grantOwnerInfo;
    }
   
    /**
     * @return
     */
    public InfoGrantType getGrantTypeInfo()
    {
        return grantTypeInfo;
    }

    /**
     * @param grantType
     */
    public void setGrantTypeInfo(InfoGrantType grantType)
    {
        this.grantTypeInfo = grantType;
    }

    /**
     * @return
     */
    public InfoGrantOrientationTeacher getGrantOrientationTeacherInfo()
    {
        return grantOrientationTeacherInfo;
    }

    /**
     * @param grantOrientationTeacherInfo
     */
    public void setGrantOrientationTeacherInfo(InfoGrantOrientationTeacher grantOrientationTeacherInfo)
    {
        this.grantOrientationTeacherInfo = grantOrientationTeacherInfo;
    }

    /**
     * @return
     */
    public InfoGrantResponsibleTeacher getGrantResponsibleTeacherInfo()
    {
        return grantResponsibleTeacherInfo;
    }

    /**
     * @param grantResponsibleTeacherInfo
     */
    public void setGrantResponsibleTeacherInfo(InfoGrantResponsibleTeacher grantResponsibleTeacherInfo)
    {
        this.grantResponsibleTeacherInfo = grantResponsibleTeacherInfo;
    }

}